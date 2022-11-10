

import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import com.google.gson.JsonObject;
import reddit.Database;

/**
 * Servlet implementation class CommentLikes
 */
public class CommentLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentLikes() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
		String username = request.getParameter("username").toLowerCase();
        String contentid= request.getParameter("contentid");
        boolean status= Boolean.parseBoolean(request.getParameter("status")); 
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
				String sql="select * from likes where contentid='"+contentid+"' and username='"+username+"' and comment=true;";
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(sql);
				int c=0;
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
				if(rs.next()) {
					c++;
					sql="update likes set status="+status+" where likeid='"+rs.getString("likeid")+"' RETURNING *;";
					ResultSet r=stmt.executeQuery(sql);
					int co=0;
					if(r.next()) {
						co++;
					JsonObject likedata=new JsonObject();
					likedata.addProperty("username",r.getString("username"));
					likedata.addProperty("status",r.getString("status").compareTo("t")==0);
					likedata.addProperty("comment",r.getString("comment").compareTo("t")==0);
					likedata.addProperty("contentid", r.getString("contentid"));
					likedata.addProperty("likeid", r.getInt("likeid"));
					likedata.addProperty("created_at", r.getString("created_at"));
					likedata.addProperty("updated_at", r.getString("updated_at"));
					res.add("data",likedata);
					res.addProperty("liked", true);
					res.addProperty("message","Comment Liked/Unliked Successful");
					finalresponse.add("data", res);
					finalresponse.addProperty("Code", 200);
					out.print(finalresponse);
					out.flush();
					}
					if(co==0) {
						finalresponse.addProperty("code", 202);
						res.addProperty("message", "Unable to Like Comments");
						res.addProperty("liked", false);
						finalresponse.add("data", res);
						System.out.println("Unable to Like Comments");
						out.print(finalresponse);
						out.flush();
					}
					}
				if(c==0) {
					sql="insert into likes(username,contentid,status,comment) values('"+username+"','"+contentid+"',"+status+",true) RETURNING *;";
					ResultSet r=stmt.executeQuery(sql);
					int co=0;
					if(r.next()) {
						co++;
					JsonObject likedata=new JsonObject();
					likedata.addProperty("username",r.getString("username"));
					likedata.addProperty("status",r.getString("status").compareTo("t")==0);
					likedata.addProperty("contentid", r.getString("contentid"));
					likedata.addProperty("likeid", r.getInt("likeid"));
					likedata.addProperty("comment",r.getString("comment").compareTo("t")==0);
					likedata.addProperty("created_at", r.getString("created_at"));
					likedata.addProperty("updated_at", r.getString("updated_at"));
					res.add("data",likedata);
					res.addProperty("liked", true);
					res.addProperty("message","Comments Liked/Unliked Successful");
					finalresponse.add("data", res);
					finalresponse.addProperty("Code", 200);
					out.print(finalresponse);
					out.flush();
					}
					if(co==0) {
						finalresponse.addProperty("code", 202);
						res.addProperty("message", "Unable to Like Comments");
						res.addProperty("liked", false);
						finalresponse.add("data", res);
						System.out.println("Unable to Like Comments");
						out.print(finalresponse);
						out.flush();
					}
				}
				
			}else {
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
				finalresponse.addProperty("code", 501);
				res.addProperty("message", "Database Connection Error");
				finalresponse.add("data", res);
				System.out.println("Database Connected Error");
				out.print(finalresponse);
				out.flush();
			}
			
		}catch(Exception e) {
			JsonObject res=new JsonObject();
			JsonObject finalresponse=new JsonObject();
			finalresponse.addProperty("code",501);
			res.addProperty("message","Unknown Error");
			finalresponse.add("data", res);
			out.print(finalresponse); 
			e.printStackTrace();
			out.flush();
		}


	}

}
