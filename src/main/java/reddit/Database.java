package reddit;

import com.google.gson.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

	public static synchronized JsonObject isUserInDB(String username,String email) throws SQLException{
		String sql="Select * from users where username='"+username+"' or email='"+email+"';";
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
			if(!StorageMethods.isUserInStorage(resultSet.getString("username"))) {
				User getU = new User(resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("created_at"), resultSet.getString("updated_at"));
				StorageMethods.setUser(getU);
			}
			if(!StorageMethods.isEmailInStorage(resultSet.getString("email"))){
				StorageMethods.addEmail(resultSet.getString("email"));
			}
		}
		return userdata;
	}

	public static synchronized Boolean isDatainDB(String username,String email) throws SQLException{
		String sql="Select * from users where username='"+username+"' or email='"+email+"';";
		statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
		JsonObject userdata=new JsonObject();
		if(resultSet.next()) {
			if(!StorageMethods.isUserInStorage(username)) {
				User getU = new User(resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("created_at"), resultSet.getString("updated_at"));
				StorageMethods.setUser(getU);
			}
			if(!StorageMethods.isEmailInStorage(email)){
				StorageMethods.addEmail(email);
			}
			return true;
		}
		return false;
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

	public static synchronized JsonObject postLikes(String contentId,String username,Boolean status) throws Exception{
//		String sql="select * from likes where contentid='"+contentId+"' and username='"+username+"' and comment=false;";
		String sql="insert into likes(username,contentid,status) values('"+username+"','"+contentId+"',"+status+") RETURNING *;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		JsonObject likeData=new JsonObject();
		if(resultSet.next()){
			likeData.addProperty("username",resultSet.getString("username"));
			likeData.addProperty("status",resultSet.getString("status").compareTo("t")==0);
			likeData.addProperty("contentid", resultSet.getString("contentid"));
			likeData.addProperty("comment",resultSet.getString("comment").compareTo("t")==0);
			likeData.addProperty("likeid", resultSet.getInt("likeid"));
			likeData.addProperty("created_at", resultSet.getString("created_at"));
			likeData.addProperty("updated_at", resultSet.getString("updated_at"));

		}
		return likeData;
	}

	public static synchronized void isLikesPresent(String contentId,String username) throws Exception{
		String sql="select * from likes where contentid='"+contentId+"' and username='"+username+"' and comment=false;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		JsonObject likeData=new JsonObject();
		if(resultSet.next()){
			likeData.addProperty("username",resultSet.getString("username"));
			likeData.addProperty("status",resultSet.getString("status").compareTo("t")==0);
			likeData.addProperty("contentid", resultSet.getString("contentid"));
			likeData.addProperty("comment",resultSet.getString("comment").compareTo("t")==0);
			likeData.addProperty("likeid", resultSet.getInt("likeid"));
			likeData.addProperty("created_at", resultSet.getString("created_at"));
			likeData.addProperty("updated_at", resultSet.getString("updated_at"));
		}
	}

	public static synchronized JsonObject getMyPosts(String username) throws Exception {
		String sql="select * from posts where created_by='"+username+"' order by created_at desc;";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
		return res;
	}
	public static synchronized JsonObject getAllPostsExcept(String username) throws Exception {
		String sql="select * from posts where created_by!='"+username+"' order by created_at desc;";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
		return res;
	}
	public static synchronized JsonObject getAllPosts() throws Exception {
		String sql="select * from posts order by created_at desc;";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
		return res;
	}

	public static synchronized void postChildCommentsBatch(String sql) throws Exception{
		sql="insert into comments(commentid,username,parentcomment,comment,postid) values "+sql+" RETURNING *;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
	}
	public static synchronized void updatePosts(String sql) throws Exception{
		sql="UPDATE posts "+sql+" RETURNING *;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
	}
	public static synchronized void updateComment(String sql) throws Exception{
		sql="UPDATE comments "+sql+" RETURNING *;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
	}

	public static synchronized JsonObject postComments(String commentID,String username,String comment,String postID) throws Exception {
//		String commentID=RandomIDGenerator(username,"Post");
		String sql="insert into comments(commentid,username,comment,postid) values('"+commentID+"','"+username+"','"+comment+"',"+postID+") RETURNING *;";
		statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
		JsonArray arr=convertToJSONComments(resultSet);
		return (JsonObject) arr.get(0);
	}
	public static synchronized void appendChildComment(String parentcomment,String commentID,String postId) throws Exception{
		String sql="update comments set childcomments=array_append(childcomments, '"+commentID+"') where commentid='"+parentcomment+"' and postid='"+postId+"' RETURNING *;";
		statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
	}
	public static synchronized void postBatchComments(String sql) throws Exception{
		sql="insert into comments(commentid,username,comment,postid) values "+sql+" RETURNING *;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
	}

	public static synchronized void postBatchPosts(String sql) throws Exception{
		sql="insert into posts(postid,created_by,content,created_at,updated_at) values "+ sql+" RETURNING *;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
	}

	public static synchronized void postBatchLikes(String sql) throws Exception{
		sql="insert into likes(likeid,contentid,username,status,comment) values "+ sql+" RETURNING *;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
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

	public static synchronized void updateLikes(String likeId,Boolean status,String username) throws Exception {
		String sql="update likes set status="+status+" where likeid='"+likeId+"' RETURNING *;";
		statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
//		JsonArray arr=convertToJSONLikes(resultSet);
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
		getLikesForContent(postId);
		JsonObject res=getCommentsForPosts(postId);
	}
	public static void getLikesForContent(String contentId) throws Exception{
		String sql="select * from likes where contentid='"+contentId+"';";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		convertToJSONLikes(resultSet);
	}
	public static synchronized void getMessagesFromConversationId(JsonObject messageData) throws Exception{
		String conversationId = messageData.get("conversationid").getAsString();
		String user1 = messageData.get("user1").getAsString();
		String user2 = messageData.get("user2").getAsString();
		Conversation convo=new Conversation(user1,user2,conversationId);
		Storage.conversations.put(conversationId,convo);
		String created_at = messageData.get("created_at").getAsString();
		StorageMethods.addConversationstoUsers(user1,user2,conversationId);
		String sql="Select * from messages where conversationid='"+conversationId+"';";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		addMessagesToInMemory(resultSet);
	}

	public static synchronized void createConversation(String user1,String user2,String conversationId) throws Exception{
		String sql="Insert into conversations(user1,user2,conversationid) values ('"+user1+"','"+user2+"','"+conversationId+"') RETURNING *";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		sql="Insert into messages(conversationid,messages) values ('"+conversationId+"','{}') RETURNING *";
		statement1=connection.createStatement();
		resultSet=statement1.executeQuery(sql);
	}
	public static synchronized void updateMessage(String conversationId,JsonObject message) throws Exception{
		String sql="Update messages set messages='"+message+"' where conversationid='"+conversationId+"' RETURNING *;";
		System.out.println(sql);
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
	}
	public static synchronized void addMessagesToInMemory(ResultSet resultSet) throws Exception{
		while (resultSet.next()){
			String conversationId=resultSet.getString("conversationid");
			JsonElement message=JsonParser.parseString(resultSet.getString("messages").toString());
			StorageMethods.addMessagesToMemory(conversationId,message);
		}
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
		public static JsonArray converToJSONMessages(ResultSet resultSet) throws Exception{
			JsonArray jsonArray = new JsonArray();
			while (resultSet.next()) {
				int total_columns = resultSet.getMetaData().getColumnCount();
				JsonObject obj = new JsonObject();
				List<String> childs=new ArrayList<>();
				for (int i = 0; i < total_columns; i++) {
					obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getString(i+1));
				}

				getMessagesFromConversationId(obj);
				jsonArray.add(obj);
			}
			return jsonArray;
		}
	public static JsonArray convertToJSONComments(ResultSet resultSet)
			throws Exception {
		JsonArray jsonArray = new JsonArray();
		while (resultSet.next()) {
			int total_columns = resultSet.getMetaData().getColumnCount();
			JsonObject obj = new JsonObject();
			ArrayList<String> childs=new ArrayList<>();
			ArrayList<String> childsNew=new ArrayList<>();
			for (int i = 0; i < total_columns; i++) {
				obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getString(i+1));
				if(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase().equalsIgnoreCase("childcomments")) {
					System.out.println("Child Comment here" +resultSet.getString(i+1));
					String s=resultSet.getString(i+1);
					String news=resultSet.getString(i+1);
					if(s.length()>2) {
						s = s.replace("{", "");
						s = s.replace("}", "");
						System.out.println(s);
						if(s.contains("\"")){
							s=s.replaceAll("\"","'");
							System.out.println(s);
						}
						news=s;
						if(news.contains("'")){
							news=news.replaceAll("'","");
						}
						String[] words = s.split(",");
						String[] newWords=news.split(",");
						Pattern pattern = Pattern.compile(" ");
						words = pattern.split(s);
						newWords=pattern.split(news);
						childs.addAll(Arrays.asList(words));
						childsNew.addAll(Arrays.asList(newWords));
						System.out.println(Arrays.toString(words));
					}
					obj.add(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), new Gson().toJsonTree(childsNew).getAsJsonArray());
				}
			}
			if(childs.size()!=0) {
				getChildComments(childs);
			}
			addToInMemoryComments(obj);
			jsonArray.add(obj);
		}
		return jsonArray;
	}
	public static void getChildComments(ArrayList<String> commentIds) throws Exception {
		System.out.println("Getting comments for comment id Array "+commentIds);
		String sql="select * from comments where commentid in (";
		for(int i=0;i<commentIds.size();i++){
			String id=commentIds.get(i).toString();
			sql=sql+id;
			if(i!=commentIds.size()-1){
				sql=sql+",";
			}
		}
		sql=sql+");";
		System.out.println(sql);
		JsonObject res=new JsonObject();
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		res.add("data",convertToJSONComments(resultSet));
		System.out.println("comments Collected for Comment id "+commentIds);
	}
	public static void convertToJSONLikes(ResultSet resultSet) throws Exception{
		JsonArray jsonArray = new JsonArray();
		while (resultSet.next()) {
			int total_columns = resultSet.getMetaData().getColumnCount();
			JsonObject obj = new JsonObject();
			for (int i = 0; i < total_columns; i++) {
				obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getString(i+1));
			}
			addToInMemoryLikes(obj);
			jsonArray.add(obj);
		}
//		return jsonArray;
	}

	public synchronized static void addToInMemoryLikes(JsonObject likeData) throws Exception{
		String username = likeData.get("username").getAsString();
		Boolean comment = likeData.get("comment").getAsString().equalsIgnoreCase("t");
		Boolean status = likeData.get("status").getAsString().equalsIgnoreCase("t");
		String contentId=likeData.get("contentid").getAsString();
		String likeid=likeData.get("likeid").getAsString();
		String created_at=likeData.get("created_at").getAsString();
		String updated_at=likeData.get("updated_at").getAsString();
		if(comment){
			Like newLike=new Like(likeid,contentId,username,status,true,created_at,updated_at);
			StorageMethods.addCommentLikes(likeid,contentId,newLike);
			if(StorageMethods.isCommentInComments(contentId)) {
				System.out.println(contentId + " " + likeid);
				StorageMethods.addLikeToComment(likeid, contentId);
			}else{
				getCommentId(contentId);
				StorageMethods.addLikeToComment(contentId, likeid);
			}
		}else{
			Like newLike=new Like(likeid,contentId,username,status,false,created_at,updated_at);
			StorageMethods.addPostLikes(likeid,contentId,newLike);
			if(StorageMethods.isPostinPosts(contentId)) {
				System.out.println(contentId + " " + likeid);
				StorageMethods.addLikeToPosts(likeid, contentId);
			}else{
				getPostID(contentId);
				StorageMethods.addLikeToPosts(contentId, likeid);
			}
		}

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
		ArrayList<String> childs=new ArrayList<>();
		for(JsonElement child:childcomments){
			childs.add(child.getAsString());
		}
		String postId=commentData.get("postid").getAsString();
		System.out.println("These are child comments "+ childs);
		Comments comments = new Comments(commentID, comment, username,postId,parrent,childs, created_at, updated_at);
		StorageMethods.addCommentToCommentByPosts(postId,comments);
		if (!StorageMethods.isCommentInComments(postId)) {
			StorageMethods.addComment(commentID,comments);
		}
		if(StorageMethods.isPostinPosts(postId)) {
			System.out.println(postId + " " + commentID);
			StorageMethods.addCommentToPosts(commentID, postId);
		}else{
			getPostID(postId);
			StorageMethods.addCommentToPosts(commentID, postId);
		}
		getLikesForContent(commentID);
	}
	public static synchronized void getPostID(String postID) throws  Exception{
		String sql="select * from posts where postid='"+postID+"';";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
	}

	public static synchronized void getCommentId(String commentId) throws Exception{
		String sql="select * from comments where commentid='"+commentId+"'";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONComments(resultSet));
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

	public static synchronized String RandomIDGenerator(String username)
	{
		String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
		String result=username+",";
		int i;
		for ( i=0; i<6; i++) {
			//generating a random number using math.random()
			int ch = (int)(AlphaNumericStr.length() * Math.random());
			//adding Random character one by one at the end of s
			result=result+AlphaNumericStr.charAt(ch);
		}
		return result;

	}

	public static synchronized String RandomIDLikeGenerator(String username)
	{
		String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
		String result=username+",Like,";
		int i;
		for ( i=0; i<6; i++) {
			//generating a random number using math.random()
			int ch = (int)(AlphaNumericStr.length() * Math.random());
			//adding Random character one by one at the end of s
			result=result+AlphaNumericStr.charAt(ch);
		}
		return result;

	}

	public static synchronized String RandomConversationID(String user1,String user2)
	{
		String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
		String result=user1+","+user2+",";
		int i;
		for ( i=0; i<6; i++) {
			//generating a random number using math.random()
			int ch = (int)(AlphaNumericStr.length() * Math.random());
			//adding Random character one by one at the end of s
			result=result+AlphaNumericStr.charAt(ch);
		}
		return result;

	}


	public static synchronized JsonObject getMyMessages(String username) throws Exception{
		String sql="select * from conversations where user1='"+username+"' or user2='"+username+"';";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",converToJSONMessages(resultSet));
		return res;
	}
}
