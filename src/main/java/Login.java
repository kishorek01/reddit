

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
 * Servlet implementation class Login
 */

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
		String username = request.getParameter("username").toLowerCase();
        String password = request.getParameter("password");
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
				String sql="Select * from users where username='"+username+"';";
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(sql);
				int c=0;
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
				if(rs.next()) {
					c++;
					if(password.compareTo(rs.getString("password"))!=0) {
						res.addProperty("message","Wrong Password");
						res.addProperty( "login", false);
						finalresponse.add("data", res);
						finalresponse.addProperty("code",203);
						System.out.println("Wrong Password");
						out.print(finalresponse);
						out.flush();
					}else {
					JsonObject userdata=new JsonObject();
					userdata.addProperty("username",rs.getString("username"));
					userdata.addProperty("name", rs.getString("name"));
					userdata.addProperty("password", rs.getString("password"));
					userdata.addProperty("email", rs.getString("email"));
					userdata.addProperty("created_at", rs.getString("created_at"));
					res.add("data",userdata);
					res.addProperty("login", true);
					res.addProperty("message","Login Successful");
					finalresponse.add("data", res);
					finalresponse.addProperty("Code", 200);
					out.print(finalresponse);
					out.flush();
					}
				}
				if(c==0) {
					finalresponse.addProperty("code", 202);
					res.addProperty("message", "Username Not Found");
					res.addProperty("login", false);
					finalresponse.add("data", res);
					System.out.println("Username Not Found");
					out.print(finalresponse);
				}
				out.flush();
				
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
