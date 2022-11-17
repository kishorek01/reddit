import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
				getFromDB(request, response, username, password);
			} else {
				User user = StorageMethods.getUser(username);
				System.out.println("Getting From In Memory");
				if (user.password.equals(password)) {

					StorageMethods.throwUser(user,request,response);
				} else {
					StorageMethods.throwWrongPassword(request, response);
					System.out.println("Wrong Password");
				}
			}
		}catch (Exception e){
			StorageMethods.throwUnknownError(request,response);
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
			StorageMethods.throwUnknownError(request,response);
		}
	}

}
