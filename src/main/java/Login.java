import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.Database;
import reddit.Storage;
import reddit.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
		String username = request.getParameter("username").toLowerCase();
        String password = request.getParameter("password");
		if (!Storage.isUserInStorage(username)) {
			getFromDB(request, response, username, password);
		}else{
			User user = Storage.getUser(username);
			System.out.println("Getting From In Memory");
			JsonObject res=new JsonObject();
			JsonObject finalResponse=new JsonObject();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out=response.getWriter();
			if(user.password.equals(password)) {
				JsonObject userData = new JsonObject();
				userData.addProperty("username", user.username);
				userData.addProperty("name", user.name);
				userData.addProperty("password", user.password);
				userData.addProperty("email", user.email);
				userData.addProperty("created_at", user.created_at);
				userData.addProperty("updated_at", user.updated_at);
				res.add("data", userData);
				res.addProperty("login", true);
				res.addProperty("message", "Login Successful");
				finalResponse.add("data", res);
				finalResponse.addProperty("Code", 200);
			}else{
				res.addProperty("message","Wrong Password");
				res.addProperty( "login", false);
				finalResponse.add("data", res);
				finalResponse.addProperty("code",203);
				System.out.println("Wrong Password");
			}
			out.print(finalResponse);
			out.flush();
			out.flush();
		}
	}

	protected void getFromDB(HttpServletRequest request,HttpServletResponse response,String username,String password) throws ServletException,IOException{
		System.out.println("Getting From DB");
		Connection connection=null;
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
			connection= Database.initializeDatabase();
			if(connection!=null) {
				String sql="Select * from users where username='"+username+"';";
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(sql);
				int c=0;
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
				if(rs.next()) {
					c++;
					if(!Storage.isUserInStorage(rs.getString("username"))) {
						User getU = new User(rs.getString("username"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("created_at"), rs.getString("updated_at"));
						Storage.setUser(getU);
					}
					if(!Storage.isEmailInStorage(rs.getString("email"))) {
						Storage.addEmail(rs.getString("email"));
					}
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
			JsonObject finalResponse=new JsonObject();
			finalResponse.addProperty("code",501);
			res.addProperty("message","Unknown Error");
			finalResponse.add("data", res);
			out.print(finalResponse);
			e.printStackTrace();
			out.flush();
		}
	}

}
