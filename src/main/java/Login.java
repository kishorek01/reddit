import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import reddit.*;

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
		try {
			if (!StorageMethods.isUserInStorage(username)) {
				System.out.println("Getting From DB Memory");

				getFromDB(request, response, username, password);
			} else {

				System.out.println("Getting From In Memory");
				User user = StorageMethods.getUser(username);
				if (user.password.equals(password)) {

					StorageMethods.throwUser(user,request,response);
				} else {
					StorageMethods.throwWrongPassword(request, response);
//					System.out.println("Wrong Password");
				}
			}
		}catch (Exception e){
			StorageMethods.throwUnknownError(request,response);
		}
	}

	protected synchronized void getFromDB(HttpServletRequest request,HttpServletResponse response,String username,String password) throws ServletException,IOException{
		System.out.println("Getting From DB");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		JsonObject data = new JsonObject();
		JsonObject finalResponse = new JsonObject();
		Database.initializeDatabase();
		try {
			System.out.println("Getting from db");
			JsonObject userData= Database.loginUser(username);
//			System.out.println(userData.has("username"));
			if(userData.has("username")){
				LoginUser loginUser=new LoginUser();
				LoginThread r1=new LoginThread(loginUser,username,password,request,response);
				r1.start();
				try{
					r1.join();
				}catch (Exception e){
					e.printStackTrace();
				}
				if(userData.get("password").getAsString().equals(password)){
					HttpSession session = request.getSession();
					session.setAttribute("username",username);
					session.setMaxInactiveInterval(10*60);
					Cookie loginCookie = new Cookie("user",username);
					loginCookie.setMaxAge(30*60);
					response.addCookie(loginCookie);
					data.add("data", userData);
					data.addProperty("login", true);
					data.addProperty("message", "Login Successful");
					finalResponse.addProperty("code", 200);
					finalResponse.add("data", data);
					out.print(finalResponse);
					out.flush();

				}else{
					StorageMethods.throwWrongPassword(request,response);

				}


			}else{
				data.addProperty("login", false);
				data.addProperty("message", "User Not Found");
				finalResponse.add("data", data);
				finalResponse.addProperty("code", 202);
				out.print(finalResponse);
				out.flush();
			}

		}catch(Exception e) {
			e.printStackTrace();
			StorageMethods.throwUnknownError(request,response);
		}
	}

}
