

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
 * Servlet implementation class CreatePost
 */
public class CreatePost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreatePost() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
		String username = request.getParameter("username").toLowerCase();
        String content = request.getParameter("content");
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
				String sql="insert into posts(created_by,content) values('"+username+"','"+content+"') RETURNING *;";
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(sql);
				int c=0;
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
				if(rs.next()) {
					c++;
					JsonObject postdata=new JsonObject();
					postdata.addProperty("created_by",rs.getString("created_by"));
					postdata.addProperty("content", rs.getString("content"));
					postdata.addProperty("postid", rs.getInt("postid"));
					postdata.addProperty("created_at", rs.getString("created_at"));
					postdata.addProperty("updated_at", rs.getString("updated_at"));
					res.add("data",postdata);
					res.addProperty("posted", true);
					res.addProperty("message","Posted Successful");
					finalresponse.add("data", res);
					finalresponse.addProperty("Code", 200);
					out.print(finalresponse);
					out.flush();
					}
				if(c==0) {
					finalresponse.addProperty("code", 202);
					res.addProperty("message", "Unable to Add Posts");
					res.addProperty("posted", false);
					finalresponse.add("data", res);
					System.out.println("Unable to Add Posts");
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
