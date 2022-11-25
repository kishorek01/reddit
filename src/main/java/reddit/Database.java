package reddit;

import com.google.gson.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
			e.printStackTrace();
//			System.out.println("Database Connected Error");
		}
	}
	public static void initializeDatabase(){

		try {
			Class.forName("org.postgresql.Driver");
			String dbURL = "jdbc:postgresql://localhost:5432/";
			String dbName = "reddit";
			String dbUsername = "postgres";
			String dbPassword = "Password";
			connection = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println("Database Connected Error");
		}
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

	public static synchronized  void deletePost(String postId) throws SQLException{
		String sql="delete from posts where postid='"+postId+"';";
		statement=connection.createStatement();
		statement.execute(sql);
		sql="delete from likes where postid='"+postId+"';";
		statement=connection.createStatement();
		statement.execute(sql);
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

	public static synchronized JsonObject loginUser(String username) throws Exception {

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
				User getU = new User(resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("created_at"), resultSet.getString("updated_at"));
				StorageMethods.setUser(getU);
				JsonObject obh=getMyPosts(resultSet.getString("username"));

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
//		String sql="select * from posts where created_by='"+username+"' order by created_at desc;";
		String sql="select * from posts p left join (select postid as pid,count(*) from likes where commentid is null group by postid) l on l.pid=p.postid where p.created_by='"+username+"' order by p.created_at desc nulls last;";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
//		System.out.println("Added");
		return res;
	}

	public static synchronized void getMySortedPosts(String username,String sortType,HttpServletRequest request,HttpServletResponse response) throws Exception {
//		String sql="select * from posts where created_by='"+username+"' order by created_at desc;";
		String sql;
		if(sortType.equalsIgnoreCase("top")){
			sql="select p.postid,l.status,l.count from posts p left join (select postid as pid,status,count(status) from likes where commentid is null group by postid,status) l on l.pid=p.postid where p.created_by='"+username+"' order by case when l.status is true then 0 when l.status is null then 1 end,case when l.status is true then l.count end desc, case when l.status is false then l.count end asc,p.created_at desc;";
		}else{
			sql="select * from posts p left join (select postid as pid,count(*) from likes where commentid is null group by postid) l on l.pid=p.postid where p.created_by='"+username+"' order by p.created_at desc nulls last;";
		}
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonArray postData;
		JsonObject res=new JsonObject();

		response.setContentType("application/json");
		JsonObject finalResponse=new JsonObject();
		ArrayList<Posts> arr=new ArrayList<>();
		JsonObject commentData=new JsonObject();
		while (rs.next()) {
			String postid=rs.getString("postid");
			StorageMethods.posts.get(postid).countLike= rs.getInt("count");
			if(StorageMethods.commentsByPostId.containsKey(postid) && StorageMethods.commentsByPostId.get(postid).size()>0) {
				StorageMethods.posts.get(postid).totalComments= StorageMethods.commentsByPostId.get(postid).size();
				commentData.add(postid, new Gson().toJsonTree(StorageMethods.commentsByPostId.get(postid)).getAsJsonObject());
			}
			arr.add(StorageMethods.posts.get(postid));

		}



		postData = new Gson().toJsonTree(arr).getAsJsonArray();
		res.add("data",postData);
		res.add("commentData",commentData);
		res.addProperty("postget", true);
		res.addProperty("message", "Post get Successful");
		finalResponse.add("data", res);
		finalResponse.addProperty("code", 200);
		PrintWriter out=response.getWriter();
		out.print(finalResponse);
		out.flush();
	}


	public static synchronized void getSortedPosts(String sortType, HttpServletRequest request,HttpServletResponse response) throws Exception{
		String sql;
		if(sortType.equalsIgnoreCase("top")){
			sql="select p.postid,l.status,l.count from posts p left join (select postid as pid,status,count(status) from likes where commentid is null group by postid,status) l on l.pid=p.postid order by case when l.status is true then 0 when l.status is null then 1 end,case when l.status is true then l.count end desc, case when l.status is false then l.count end asc,p.created_at desc;";
		}else{
			sql="select p.postid,l.count from posts p left join (select postid as pid,count(*) from likes where commentid is null group by postid) l on l.pid=p.postid order by p.created_at desc nulls last;";
		}

		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonArray postData;
		response.setContentType("application/json");
		JsonObject finalResponse=new JsonObject();
		ArrayList<Posts> arr=new ArrayList<>();
		while (rs.next()) {
			String postid=rs.getString("postid");
			StorageMethods.posts.get(postid).countLike= rs.getInt("count");
			if(StorageMethods.commentsByPostId.containsKey(postid)) {
				StorageMethods.posts.get(postid).totalComments = StorageMethods.commentsByPostId.get(postid).size();
			}
			if(!arr.contains(StorageMethods.posts.get(postid))) {
				arr.add(StorageMethods.posts.get(postid));
			}
		}

		postData = new Gson().toJsonTree(arr).getAsJsonArray();
		finalResponse.addProperty("postget", true);
		finalResponse.addProperty("message", "Post get Successful");
		finalResponse.add("data", postData);
		finalResponse.addProperty("code", 200);
		PrintWriter out=response.getWriter();
		out.print(finalResponse);
		out.flush();
	}


	public static synchronized void getChildComment(String postId,String parentcomment,String sortType,HttpServletRequest request, HttpServletResponse response) throws  Exception{
		String sql="SELECT"+
				" p1.commentid as l1id,"+
				" p2.commentid as l2id,"+
				" p3.commentid as l3id,"+
				" p4.commentid as l4id"+
				" FROM"+
				" comments p1"+
				" LEFT JOIN"+
				" comments p2 on p2.parentcomment = p1.commentid"+
				" LEFT JOIN"+
				" comments p3 on p3.parentcomment = p2.commentid"+
				" LEFT JOIN"+
				" comments p4 on p4.parentcomment = p3.commentid where ";
		if(parentcomment==null || parentcomment.equalsIgnoreCase("null")){
			sql=sql+"p1.parentcomment is "+null+" and ";
		}else {
			sql=sql+"p1.parentcomment='"+parentcomment+"' and ";
		}
		sql=sql+ "p1.postid='"+postId+"' order by p1.created_at desc,p2.created_at desc,p3.created_at desc,p4.created_at desc;";
		Statement stmt1=connection.createStatement();
		ResultSet rs1=stmt1.executeQuery(sql);
		try {
			JsonObject res=new JsonObject();
			response.setContentType("application/json");
			JsonObject finalResponse=new JsonObject();
			Posts post=StorageMethods.posts.get(postId);
			JsonArray likeArr=new JsonArray();

			res.add("post",new Gson().toJsonTree(post,Posts.class).getAsJsonObject());
			JsonObject commentData=new JsonObject();
			JsonObject likeData=new JsonObject();
			while(rs1.next()){
				String l1id=rs1.getString("l1id");
				String l2id=rs1.getString("l2id");
				String l3id=rs1.getString("l3id");
				String l4id=rs1.getString("l4id");
				if(StorageMethods.commentsByPostId.containsKey(postId)) {
					if(l1id!=null) {
						commentData.add(l1id, new Gson().toJsonTree(StorageMethods.comments.get(l1id), Comments.class).getAsJsonObject());
					}
					if(l2id!=null) {
						commentData.add(l2id, new Gson().toJsonTree(StorageMethods.comments.get(l2id), Comments.class).getAsJsonObject());
					}
					if(l3id!=null){
						commentData.add(l3id,new Gson().toJsonTree(StorageMethods.comments.get(l3id),Comments.class).getAsJsonObject());
					}
					if(l4id!=null
					) {
						commentData.add(l4id, new Gson().toJsonTree(StorageMethods.comments.get(l4id), Comments.class).getAsJsonObject());
					}
				}
			}
			res.add("comments",commentData);
			if(StorageMethods.likesByContentId.containsKey(postId)) {
				res.add("likesobj", new Gson().toJsonTree(StorageMethods.likesByContentId.get(postId)).getAsJsonObject());
			}
			PrintWriter out=response.getWriter();
			finalResponse.add("data", res);
			finalResponse.addProperty("code", 200);
			out.print(finalResponse);
			out.flush();
		}catch (Exception e){
			e.printStackTrace();
		}
	}






	public static synchronized void getSortedPost(String postId,String sortType,String parentcomment,int depth, HttpServletRequest request,HttpServletResponse response) throws Exception{
		String sql;
		if(sortType.equalsIgnoreCase("default")){
			sql="select parentcomment,commentid from comments where parentcomment!='' order by created_at;";
		}else if(sortType.equalsIgnoreCase("top")){
//			sql="select c.parentcomment,c.commentid,l.status,l.count from comments c left join (select commentid,status,count(likes.status),array_agg(likeid) from likes group by commentid,status order by status desc,count(*) desc) l on c.commentid=l.commentid where c.postid='"+postId+"' and c.parentcomment!='' order by case when l.status is true then 0 when l.status is null then 1 end,case when l.status is true then l.count end desc, case when l.status is false then l.count end asc,c.created_at;";
			sql="select c.parentcomment,c.commentid,l.ccid,l.status,l.count from comments c left join (select likes.commentid,lab.ccid,status,count(likes.status),array_agg(likeid) from likes left join (select count(*) as ccid,commentid from likes group by commentid) lab on lab.commentid=likes.commentid group by likes.commentid,lab.ccid,likes.status order by likes.status desc,count(*) desc) l on c.commentid=l.commentid where c.postid='"+postId+"' and c.parentcomment!='' group by c.commentid,l.status,l.ccid,l.count order by case when l.status is true then 0 when l.status is null then 1 end,case when l.status is true then l.count end desc,case when l.status is true then l.ccid end, case when l.status is false then l.count end asc,case when l.status is false then l.ccid end desc,c.created_at;";
		}else{
			sql="select parentcomment,commentid from comments where parentcomment!='' order by created_at desc;";
		}

		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		ConcurrentHashMap<String,ArrayList<String>> newOrder=new ConcurrentHashMap<>();
		while (rs.next()) {
			String pcomment = rs.getString("parentcomment");
			String cid = rs.getString("commentid");
			if(!newOrder.containsKey(pcomment)){
				newOrder.put(pcomment,new ArrayList<>());
			}
			if(!newOrder.get(pcomment).contains(cid)){
				newOrder.get(pcomment).add(cid);
			}
		}

		Set<String> newpid=newOrder.keySet();
		for(String key:newpid){
			if(StorageMethods.comments.containsKey(key)) {
				StorageMethods.comments.get(key).childcomments = newOrder.get(key);
			}
		}
		if(sortType.equalsIgnoreCase("default")){
			sql="select parentcomment,commentid from comments where postid='"+postId+"' and parentcomment is null order by created_at;";
		}else if(sortType.equalsIgnoreCase("top")){
//			sql="select c.parentcomment,c.commentid,l.status,l.count from comments c left join (select commentid,status,count(likes.status),array_agg(likeid) from likes group by commentid,status order by status desc,count(*) desc) l on c.commentid=l.commentid where c.postid='"+postId+"' and c.parentcomment is null order by case when l.status is true then 0 when l.status is null then 1 end,case when l.status is true then l.count end desc, case when l.status is false then l.count end asc,c.created_at;";
			sql="select c.parentcomment,c.commentid,l.ccid,l.status,l.count from comments c left join (select likes.commentid,lab.ccid,status,count(likes.status),array_agg(likeid) from likes left join (select count(*) as ccid,commentid from likes group by commentid) lab on lab.commentid=likes.commentid group by likes.commentid,lab.ccid,likes.status order by likes.status desc,count(*) desc) l on c.commentid=l.commentid where c.postid='"+postId+"' and c.parentcomment is null group by c.commentid,l.status,l.ccid,l.count order by case when l.status is true then 0 when l.status is null then 1 end,case when l.status is true then l.count end desc,case when l.status is true then l.ccid end, case when l.status is false then l.count end asc,case when l.status is false then l.ccid end desc,c.created_at;";
		}else if(sortType.equalsIgnoreCase("new")){
			sql="select parentcomment,commentid from comments where postid='"+postId+"' and parentcomment is null order by created_at desc;";
		}

		Statement stmtpar=connection.createStatement();
		ResultSet rspar=stmt.executeQuery(sql);
		ArrayList<String> orderParent=new ArrayList<>();
		while(rspar.next()){
			String l = rspar.getString("commentid");
			if(!orderParent.contains(l)) {
				orderParent.add(l);
			}
		}
		if(StorageMethods.posts.containsKey(postId)){
			StorageMethods.posts.get(postId).comments=orderParent;
		}
//		JsonArray postData;
//		response.setContentType("application/json");
//		JsonObject finalResponse=new JsonObject();
//		ArrayList<Posts> arr=new ArrayList<>();

		sql="SELECT ";
				for(int i=1;i<=depth;i++){
					if(i!=depth) {
						sql += "p" + i + ".commentid as l" + i + "id, ";
					}
					else{
						sql += "p" + i + ".commentid as l" + i + "id ";
					}
				}
         sql+=" FROM"+
              " comments p1";
		for(int i=2;i<=depth;i++){
			if(i!=depth) {
				sql += " LEFT JOIN comments p"+i+" on p"+i+".parentcomment = p"+(i-1)+".commentid ";
			}
			else{
				sql += " LEFT JOIN comments p"+i+" on p"+i+".parentcomment = p"+(i-1)+".commentid where ";
			}
		}
				if(parentcomment==null || parentcomment.equalsIgnoreCase("null")){
					sql=sql+"p1.parentcomment is "+null+" and ";
				}else {
					sql=sql+"p1.parentcomment='"+parentcomment+"' and ";
				}
				sql=sql+ "p1.postid='"+postId+"' order by ";
		for(int i=1;i<=depth;i++){
			if(i!=depth) {
				sql += "p"+i+".created_at desc,";
			}
			else{
				sql += "p"+i+".created_at desc;";
			}
		}
		Statement stmt1=connection.createStatement();
		ResultSet rs1=stmt1.executeQuery(sql);
		try {
			JsonObject res=new JsonObject();
			response.setContentType("application/json");
			JsonObject finalResponse=new JsonObject();
			Posts post=StorageMethods.posts.get(postId);
			JsonArray likeArr=new JsonArray();

			res.add("post",new Gson().toJsonTree(post,Posts.class).getAsJsonObject());
			JsonObject commentData=new JsonObject();
			JsonObject likeData=new JsonObject();
//			StorageMethods.getPost(postId, request, response);
			while(rs1.next()){

				for(int i=1;i<=depth;i++){
					String lid=rs1.getString("l"+i+"id");
					if(StorageMethods.commentsByPostId.containsKey(postId)) {
						if(lid!=null) {
							commentData.add(lid, new Gson().toJsonTree(StorageMethods.comments.get(lid), Comments.class).getAsJsonObject());

						}
					}
				}
//				String l1id=rs1.getString("l1id");
//				String l2id=rs1.getString("l2id");
//				String l3id=rs1.getString("l3id");
//				String l4id=rs1.getString("l4id");
//				if(StorageMethods.commentsByPostId.containsKey(postId)) {
//					if(l1id!=null) {
//						commentData.add(l1id, new Gson().toJsonTree(StorageMethods.comments.get(l1id), Comments.class).getAsJsonObject());
//					}
//					if(l2id!=null) {
//						commentData.add(l2id, new Gson().toJsonTree(StorageMethods.comments.get(l2id), Comments.class).getAsJsonObject());
//					}
//					if(l3id!=null){
//						commentData.add(l3id,new Gson().toJsonTree(StorageMethods.comments.get(l3id),Comments.class).getAsJsonObject());
//					}
//					if(l4id!=null
//					) {
//						commentData.add(l4id, new Gson().toJsonTree(StorageMethods.comments.get(l4id), Comments.class).getAsJsonObject());
//					}
//				}
			}
			res.add("comments",commentData);
//			System.out.println(commentData);
			if(StorageMethods.likesByContentId.containsKey(postId)) {
				res.add("likesobj", new Gson().toJsonTree(StorageMethods.likesByContentId.get(postId)).getAsJsonObject());
//				for (String key : StorageMethods.likesByContentId.get(postId).keySet()) {
//					if(StorageMethods.likes.containsKey(key)) {
//						likeArr.add(new Gson().toJsonTree(StorageMethods.likes.get(key)).getAsJsonObject());
//					}
//				}
			}
//			res.add("likes",likeArr);
//        res.add("data",message);
			if(parentcomment!=null){
				res.add("parentcomment",new Gson().toJsonTree(StorageMethods.comments.get(parentcomment)).getAsJsonObject());
			}
			PrintWriter out=response.getWriter();
			finalResponse.add("data", res);
			finalResponse.addProperty("code", 200);
			out.print(finalResponse);
			out.flush();
		}catch (Exception e){
			e.printStackTrace();
		}
//		postData = new Gson().toJsonTree(arr).getAsJsonArray();
//		finalResponse.addProperty("postget", true);
//		finalResponse.addProperty("message", "Post get Successful");
//		finalResponse.add("data", postData);
//		finalResponse.addProperty("code", 200);
//		PrintWriter out=response.getWriter();
//		out.print(finalResponse);
//		out.flush();
	}
	public static synchronized JsonObject getAllPostsExcept(String username) throws Exception {
		String sql="select * from posts p left join (select postid as pid,count(*) from likes where commentid is null group by postid) l on l.pid=p.postid where p.created_by!='"+username+"';";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
		return res;
	}
	public static synchronized JsonObject getAllPosts() throws Exception {
		String sql="select * from posts p left join (select postid as pid,count(*) from likes where commentid is null group by postid) l on l.pid=p.postid order by p.created_at desc nulls last;";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
		return res;
	}

	public static synchronized void postChildCommentsBatch(String sql) throws Exception{
		sql="insert into comments(commentid,username,parentcomment,comment,postid) values "+sql+" on conflict(commentid) do nothing RETURNING *;";
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
		JsonArray arr=convertToJSONComments(resultSet,true);
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
		sql="insert into posts(postid,created_by,content,created_at,updated_at) values "+ sql+" on conflict(postid) do nothing RETURNING *;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
	}

	public static synchronized void postBatchLikes(String sql) throws Exception{
		sql="insert into likes(likeid,postid,username,status,commentid) values "+ sql+" on conflict(likeid) do nothing RETURNING *;";
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
		int countLike;
		if(postsData.get("count").isJsonNull()){
			countLike=0;
		}else{
			countLike = postsData.get("count").getAsInt();
		}
		String sql="select count(*) from comments where postid='"+postId+"';";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		int totalComments=0;
		while (resultSet.next()){
			totalComments=resultSet.getInt("count");
		}
		Posts post = new Posts(postId, content, created_by, created_at, updated_at,countLike,totalComments,0,0);
		if (!StorageMethods.isPostinPosts(postId)) {
			StorageMethods.addPost(postId, post);
		}
//	if(StorageMethods.isUserInStorage(created_by)) {
//////			System.out.println(postId+" "+created_by);
			StorageMethods.addPostToUsers(created_by, postId);
////		}else{
//			loginUser(created_by);
//			StorageMethods.addPostToUsers(created_by, postId);
////		}

		JsonObject res=getCommentsForPosts(postId);
		getLikesForContent(postId);
	}
	public static void getLikesForContent(String postId) throws Exception{
		String sql="select * from likes where postid='"+postId+"';";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		convertToJSONLikes(resultSet);
	}
	public static synchronized void getMessagesFromConversationId(JsonObject messageData) throws Exception{
		String conversationId = messageData.get("conversationid").getAsString();
		String user1 = messageData.get("user1").getAsString();
		String user2 = messageData.get("user2").getAsString();
		String created_at = messageData.get("created_at").getAsString();
		String updated_at = messageData.get("updated_at").getAsString();
		StorageMethods.addConversationstoUsers(user1,user2,conversationId);
		Conversation convo=new Conversation(user1,user2,conversationId,created_at,updated_at);
		Storage.conversations.put(conversationId,convo);
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
//		System.out.println(sql);
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);

		sql="Update conversations set conversationid='"+conversationId+"' where conversationid='"+conversationId+"' RETURNING *;";
//		System.out.println(sql);
		statement1=connection.createStatement();
		ResultSet resultSet1=statement1.executeQuery(sql);
	}
	public static synchronized void addMessagesToInMemory(ResultSet resultSet) throws Exception{
		while (resultSet.next()){
			String conversationId=resultSet.getString("conversationid");
			JsonElement message=JsonParser.parseString(resultSet.getString("messages").toString());
			String created_at=resultSet.getString("created_at");
			StorageMethods.addMessagesToMemory(conversationId,message,created_at);
		}
	}
		public static synchronized JsonObject getCommentsForPosts(String postId) throws Exception {
//			System.out.println("Getting comments for post id "+postId);
			String sql="select * from comments where postid='"+postId+"' order by created_at;";
			JsonObject res=new JsonObject();
			Statement statement1=connection.createStatement();
			ResultSet resultSet=statement1.executeQuery(sql);
			res.add("data",convertToJSONComments(resultSet,true));
//			System.out.println("comments Collected for post id "+postId);
			return  res;
		}
		public static JsonArray converToJSONMessages(ResultSet resultSet) throws Exception{
			JsonArray jsonArray = new JsonArray();
			while (resultSet.next()) {
				int total_columns = resultSet.getMetaData().getColumnCount();
				JsonObject obj = new JsonObject();
				for (int i = 0; i < total_columns; i++) {
					obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getString(i+1));
				}

				getMessagesFromConversationId(obj);
//				System.out.println(obj);
				jsonArray.add(obj);
			}
			return jsonArray;
		}
	public static JsonArray convertToJSONComments(ResultSet resultSet,Boolean status)
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
//					System.out.println("Child Comment here" +resultSet.getString(i+1));
					Array s=resultSet.getArray(i+1);
//					System.out.println(Arrays.asList((String[]) s.getArray()));
					String news=resultSet.getString(i+1);
					childsNew.addAll(Arrays.asList((String[]) s.getArray()));
//					if(s.length()>2) {
//						s = s.replace("{", "");
//						s = s.replace("}", "");
////						System.out.println(s);
//						if(s.contains("\"")){
//							s=s.replaceAll("\"","'");
//							System.out.println(s);
//						}
//						news=s;
//						if(news.contains("'")){
//							news=news.replaceAll("'","");
//						}
//						String[] words = s.split(",");
//						String[] newWords=news.split(",");
//						Pattern pattern = Pattern.compile(" ");
//						words = pattern.split(s);
//						newWords=pattern.split(news);
//						childs.addAll(Arrays.asList(words));
//						childsNew.addAll(Arrays.asList(newWords));
//						System.out.println(Arrays.toString(words));
//					}
					JsonArray nre=new JsonArray();
					for(String child:childsNew){
						nre.add(child);
						if(!nre.contains(new Gson().toJsonTree(child))) {
							System.out.println(child);
							getChildComments(child);
						}
					}
					obj.add(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), nre);
				}
			}
//			if(childsNew.size()!=0) {
//				getChildComments(childsNew);
//			}
//			System.out.println(((Object)childsNew).getClass().getSimpleName()+" "+childsNew.size());
			addToInMemoryComments(obj,status,childsNew);
			jsonArray.add(obj);
		}
		return jsonArray;
	}
	public static void getChildComments(String commentId) throws Exception {
//		System.out.println("Getting comments for comment id Array "+commentIds);
		String sql="select * from comments where commentid='"+commentId+"' order by created_at;";
//		System.out.println(sql);
		JsonObject res=new JsonObject();
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		res.add("data",convertToJSONComments(resultSet,false));
//		System.out.println("comments Collected for Comment id "+commentIds);
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
		String commentid;
		if(likeData.get("commentid").isJsonNull()){
			commentid=null;
		}else{
			commentid = likeData.get("commentid").getAsString();
		}

		Boolean status = likeData.get("status").getAsString().equalsIgnoreCase("t");
		String postid=likeData.get("postid").getAsString();
		String likeid=likeData.get("likeid").getAsString();
		String created_at=likeData.get("created_at").getAsString();
		String updated_at=likeData.get("updated_at").getAsString();

			Like newLike=new Like(likeid,postid,username,status,commentid,created_at,updated_at);
			StorageMethods.addPostLikes(likeid,postid,newLike);
			if(commentid==null) {
				if (StorageMethods.isPostinPosts(postid)) {
//				System.out.println(postid + " " + likeid);
					StorageMethods.addLikeToPosts(likeid, postid,status);
				} else {
					getPostID(postid);
					StorageMethods.addLikeToPosts(postid, likeid,status);
				}

			}

			if(commentid!=null){
				if(StorageMethods.isCommentInComments(commentid)){
					StorageMethods.addLikeToComment(likeid,commentid,status);
				}else{
					getCommentId(commentid);
					StorageMethods.addLikeToComment(likeid,commentid,status);
				}
			}


	}

	public synchronized static void addToInMemoryComments(JsonObject commentData,Boolean status,ArrayList<String> children) throws Exception {
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

//		System.out.println("Childs Here are"+ children);
		String postId=commentData.get("postid").getAsString();
		Comments comments = new Comments(commentID, comment, username,postId,parrent,children, created_at, updated_at);
		StorageMethods.addCommentToCommentByPosts(postId,comments);
		if (!StorageMethods.isCommentInComments(postId)) {
			StorageMethods.addComment(commentID,comments);
		}
		if(status && parrent ==null) {
				StorageMethods.addCommentToPosts(commentID, postId);
		}
//		getLikesForContent(postId);
	}
	public static synchronized void getPostID(String postID) throws  Exception{
//		String sql="select * from posts where postid='"+postID+"';";
		String sql="select * from posts p left join (select postid as pid,count(*) from likes where commentid is null group by postid) l on l.pid=p.postid where p.postid='"+postID+"';";
		Statement stmt=connection.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONPosts(rs));
	}

	public static synchronized void getCommentId(String commentId) throws Exception{
		String sql="select * from comments where commentid='"+commentId+"' order by created_at";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",convertToJSONComments(resultSet,false));
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
//		System.out.println("Getting The Messages");
		String sql="select * from conversations where user1='"+username+"' or user2='"+username+"';";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		JsonObject res=new JsonObject();
		res.add("data",converToJSONMessages(resultSet));
		return res;
	}


	public static synchronized JsonArray getConvo(String username) throws Exception{
		String sql="select c.conversationid,c.user1,c.user2,c.created_at,m.updated_at from conversations c left join messages m on c.conversationid=m.conversationid where c.user1='"+username+"' or c.user2='"+username+"' order by m.updated_at desc;";
		Statement statement1=connection.createStatement();
		ResultSet resultSet=statement1.executeQuery(sql);
		JsonArray data=new JsonArray();
		while(resultSet.next()){
			JsonObject obj=new JsonObject();
			String user1= resultSet.getString("user1");
			String user2= resultSet.getString("user2");
			if(Objects.equals(username, user1)){
				obj.addProperty("username",user2);
			}else{
				obj.addProperty("username",user1);
			}

			obj.addProperty("conversationid",resultSet.getString("conversationid"));
			obj.addProperty("created_at",resultSet.getString("created_at"));
			obj.addProperty("updated_at",resultSet.getString("updated_at"));
			data.add(obj);
		}
		return data;
	}
}
