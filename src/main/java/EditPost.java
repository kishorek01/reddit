import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.Database;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class EditPost
 */
public class EditPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditPost() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
        String content = request.getParameter("content");
        String pid= request.getParameter("postid");
        int postid=Integer.parseInt(pid);
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
				String sql="update posts set content='"+content+"' where postid='"+postid+"' RETURNING *;";
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
					res.addProperty("edited", true);
					res.addProperty("message","Post Edited Successful");
					finalresponse.add("data", res);
					finalresponse.addProperty("Code", 200);
					out.print(finalresponse);
					out.flush();
					}
				if(c==0) {
					finalresponse.addProperty("code", 202);
					res.addProperty("message", "Unable to Edit Posts");
					res.addProperty("edited", false);
					finalresponse.add("data", res);
					System.out.println("Unable to Edit Posts");
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
			StorageMethods.throwUnknownError(request,response);
		}

	}

}
