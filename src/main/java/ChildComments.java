

import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.Database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class ChildComments
 */
public class ChildComments extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChildComments() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection connection=null;
		String username = request.getParameter("username").toLowerCase();
        String comment = request.getParameter("comment");
        String parentcomment = request.getParameter("parentcomment");
        int postid = Integer.parseInt(request.getParameter("postid"));
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
				String sql="insert into comments(username,parentcomment,comment,postid) values('"+username+"',"+parentcomment+",'"+comment+"',"+postid+") RETURNING *;";
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(sql);
				int c=0;
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
				if(rs.next()) {
					c++;
					
					JsonObject postdata=new JsonObject();
					postdata.addProperty("username",rs.getString("username"));
					postdata.addProperty("comment", rs.getString("comment"));
					postdata.addProperty("commentid", rs.getString("commentid"));
					postdata.addProperty("parentcomment", rs.getInt("parentcomment"));
					JsonArray arr=new JsonArray();
					postdata.add("childcomments", arr);
					postdata.addProperty("postid", rs.getInt("postid"));
					postdata.addProperty("created_at", rs.getString("created_at"));
					postdata.addProperty("updated_at", rs.getString("updated_at"));
					res.add("data",postdata);
					res.addProperty("commented", true);
					res.addProperty("message","Commented Successful");
					finalresponse.add("data", res);
					finalresponse.addProperty("Code", 200);
					String append="update comments set childcomments=array_append(childcomments, "+rs.getInt("commentid")+") where commentid="+parentcomment+" and postid="+postid+" RETURNING *;";
					ResultSet app=stmt.executeQuery(append);
					out.print(finalresponse);
					out.flush();
					}
				if(c==0) {
					finalresponse.addProperty("code", 202);
					res.addProperty("message", "Unable to Add Comments");
					res.addProperty("commented", false);
					finalresponse.add("data", res);
					System.out.println("Unable to Add Comments");
					out.print(finalresponse);
					out.flush();
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
