package reddit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Database {
	public static Connection connection=null;
	static PreparedStatement preparedStatement;
	static Statement statement;
	static {
		try {
			Class.forName("org.postgresql.Driver");
			String dbURL = "jdbc:postgresql://localhost:5432/";
			String dbName = "reddit";
			String dbUsername = "postgres";
			String dbPassword = "Password";
			connection = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
		} catch (Exception e) {
			JsonObject res = new JsonObject();
			JsonObject finalResponse = new JsonObject();
			finalResponse.addProperty("code", 501);
			res.addProperty("message", "Database Connection Error");
			finalResponse.add("data", res);
			System.out.println("Database Connected Error");
		}
	}
	public static Connection initializeDatabase(){
		return connection;
	}

	public static synchronized JsonObject registerUser(String username,String name,String email,String password) throws SQLException {
		JsonObject data = new JsonObject();
		JsonObject finalResponse = new JsonObject();
		String createUserQuery="insert into users(username,email,password,name) values('"+username+"','"+email+"','"+password+"','"+name+"') RETURNING *;";
		preparedStatement = connection.prepareStatement(createUserQuery);
		ResultSet resultSet=preparedStatement.executeQuery();
		finalResponse.addProperty("code", 200);
		data.addProperty("message", "User Created Successfully");
		JsonObject userdata=new JsonObject();
		if(resultSet.next()) {
			userdata.addProperty("username",resultSet.getString("username"));
			userdata.addProperty("name", resultSet.getString("name"));
			userdata.addProperty("password", resultSet.getString("password"));
			userdata.addProperty("email", resultSet.getString("email"));
			userdata.addProperty("created_at", resultSet.getString("created_at"));
			userdata.addProperty("updated_at", resultSet.getString("updated_at"));
			if(!StorageMethods.isUserInStorage(username)) {
				User getU = new User(resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("created_at"), resultSet.getString("updated_at"));
				StorageMethods.setUser(getU);
			}
		}
		data.add("data",userdata);
		finalResponse.add("data", data);
		return finalResponse;
	}

	public static synchronized JsonObject loginUser(String username) throws SQLException{

		String sql="Select * from users where username='"+username+"';";
		statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
		JsonObject userdata=new JsonObject();
		if(resultSet.next()) {
			userdata.addProperty("username",resultSet.getString("username"));
			userdata.addProperty("name", resultSet.getString("name"));
			userdata.addProperty("password", resultSet.getString("password"));
			userdata.addProperty("email", resultSet.getString("email"));
			userdata.addProperty("created_at", resultSet.getString("created_at"));
			userdata.addProperty("updated_at", resultSet.getString("updated_at"));
			if(!StorageMethods.isUserInStorage(username)) {
				User getU = new User(resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("created_at"), resultSet.getString("updated_at"));
				StorageMethods.setUser(getU);
			}
		}
		return userdata;
	}

	public static synchronized JsonObject getMyPosts(String username) throws Exception {
		String sql="select * from posts where created_by='"+username+"';";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
		return res;
	}

	public static synchronized JsonObject postComments(String username,String comment,String postID) throws Exception {
		String commentID=RandomIDGenerator(username,"Post");
		String sql="insert into comments(commentid,username,comment,postid) values('"+commentID+"','"+username+"','"+comment+"',"+postID+") RETURNING *;";
		statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
		JsonArray arr=convertToJSONComments(resultSet);
		return (JsonObject) arr.get(0);
	}
	public static synchronized JsonArray convertToJSONPosts(ResultSet resultSet)
			throws Exception {
		JsonArray jsonArray = new JsonArray();
		while (resultSet.next()) {
			int total_columns = resultSet.getMetaData().getColumnCount();
			JsonObject obj = new JsonObject();
			for (int i = 0; i < total_columns; i++) {
				obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getString(i+1));
			}
			addToInmemoryPosts(obj);
			jsonArray.add(obj);
		}
		return jsonArray;
	}

	public static synchronized void addToInmemoryPosts(JsonObject postsData) throws Exception {
		String created_by = postsData.get("created_by").getAsString();
		String content = postsData.get("content").getAsString();
		String created_at = postsData.get("created_at").getAsString();
		String updated_at = postsData.get("updated_at").getAsString();
		String postId = postsData.get("postid").getAsString();
		Posts post = new Posts(postId, content, created_by, created_at, updated_at);
		if (!StorageMethods.isPostinPosts(postId)) {
			StorageMethods.addPost(postId, post);
		}
		if(StorageMethods.isUserInStorage(created_by)) {
			System.out.println(postId+" "+created_by);
			StorageMethods.addPostToUsers(created_by, postId);
		}else{
			loginUser(created_by);
			StorageMethods.addPostToUsers(created_by, postId);
		}
		JsonObject res=getCommentsForPosts(postId);
	}
		public static synchronized JsonObject getCommentsForPosts(String postId) throws Exception {
			System.out.println("Getting comments for post id "+postId);
			String sql="select * from comments where postid='"+postId+"';";
			JsonObject res=new JsonObject();
			Statement statement1=connection.createStatement();
			ResultSet resultSet=statement1.executeQuery(sql);
			res.add("data",convertToJSONComments(resultSet));
			System.out.println("comments Collected for post id "+postId);
			return  res;
		}
	public static JsonArray convertToJSONComments(ResultSet resultSet)
			throws Exception {
		JsonArray jsonArray = new JsonArray();
		while (resultSet.next()) {
			int total_columns = resultSet.getMetaData().getColumnCount();
			JsonObject obj = new JsonObject();
			for (int i = 0; i < total_columns; i++) {
				obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getString(i+1));
				if(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase().equalsIgnoreCase("childcomments")) {
					String s=resultSet.getString(i+1);
					s=s.replace('{', ' ');
					s=s.replace('}',' ');
					String[] words = s.split(",");
					Pattern pattern = Pattern.compile(" ");
					words = pattern.split(s);
					System.out.println(Arrays.toString(words));
					obj.add(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), new Gson().toJsonTree(words).getAsJsonArray());
				}
			}
			addToInMemoryComments(obj);
			jsonArray.add(obj);
		}
		return jsonArray;
	}

	public synchronized static void addToInMemoryComments(JsonObject commentData) throws Exception {
		String username = commentData.get("username").getAsString();
		String comment = commentData.get("comment").getAsString();
		String created_at = commentData.get("created_at").getAsString();
		String updated_at = commentData.get("updated_at").getAsString();
		String commentID = commentData.get("commentid").getAsString();
		String parrent=null;
		if(!commentData.get("parentcomment").isJsonNull()) {
			parrent = commentData.get("parentcomment").getAsString();
		}
		JsonArray childcomments=commentData.get("childcomments").getAsJsonArray();
		String postId=commentData.get("postid").getAsString();
		Comments comments = new Comments(commentID, comment, username,postId,parrent,childcomments, created_at, updated_at);
		if (!StorageMethods.isCommentinComments(postId)) {
			StorageMethods.addComment(commentID,comments);
		}
		if(StorageMethods.isPostinPosts(postId)) {
			System.out.println(postId + " " + commentID);
			StorageMethods.addCommentToPosts(commentID, postId);
		}else{
			getPostID(postId);
			StorageMethods.addCommentToPosts(commentID, postId);
		}
	}
	public static synchronized void getPostID(String postID) throws  Exception{
		String sql="select * from posts where postid='"+postID+"';";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
	}

	public static synchronized String RandomIDGenerator(String username,String idType)
	{
		String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
		String result=username+","+idType+","+"";
		int i;
		for ( i=0; i<6; i++) {
			//generating a random number using math.random()
			int ch = (int)(AlphaNumericStr.length() * Math.random());
			//adding Random character one by one at the end of s
			result=result+AlphaNumericStr.charAt(ch);
		}
		return result;

	}

}
