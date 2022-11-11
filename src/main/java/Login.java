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
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		JsonObject data = new JsonObject();
		JsonObject finalResponse = new JsonObject();
		try {
			JsonObject userData= Database.loginUser(username);
			if(userData.size()!=0){
				if(userData.get("password").equals(password)){
					data.add("data", userData);
					data.addProperty("login", true);
					data.addProperty("message", "Login Successful");
				}else{
					data.addProperty("login", false);
					data.addProperty("message", "Wrong Password");
				}
				finalResponse.add("data", data);
			}else{
				data.addProperty("login", false);
				data.addProperty("message", "User Not Found");
				finalResponse.add("data", data);
			}
			out.print(finalResponse);
			out.flush();
		}catch(Exception e) {
			finalResponse.addProperty("code",501);
			data.addProperty("message","Unknown Error");
			finalResponse.add("data", data);
			out.print(finalResponse);
			e.printStackTrace();
			out.flush();
		}
	}

}
