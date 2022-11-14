import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.*;

import java.io.IOException;
import java.io.PrintWriter;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username").toString().toLowerCase();
		String email = request.getParameter("email").toString();
		String password = request.getParameter("password").toString();
		String name = request.getParameter("name").toString();
		if(StorageMethods.isEmailInStorage(email)){
			StorageMethods.throwEmailAlreadyExists(request,response);
		} else if (!StorageMethods.isUserInStorage(username)) {
			createFromDB(request, response, username, email, password, name);
		}else{
			StorageMethods.throwUsernameAlreadyExists(request,response);

		}
	}


	protected void createFromDB(HttpServletRequest request, HttpServletResponse response,String username,String email,String password, String name) throws ServletException, IOException{
		System.out.println("Creating in DB");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		JsonObject res=new JsonObject();
		JsonObject finalResponse=new JsonObject();
		try {
			JsonObject checkData=Database.isUserInDB(username,email);
			if(checkData.isJsonNull()) {
				if (username.equalsIgnoreCase(checkData.get("username").getAsString())) {
					StorageMethods.throwUsernameAlreadyExists(request,response);
					if (!StorageMethods.isUserInStorage(username)) {
						User getU = new User(username, name, email, password, checkData.get("created_at").getAsString(), checkData.get("updated_at").getAsString());
						StorageMethods.setUser(getU);
					}
					if (!StorageMethods.isEmailInStorage(email)) {
						StorageMethods.addEmail(email);
					}
					out.print(finalResponse);
				} else if (email.equals(checkData.get("email").getAsString())) {
					if (!StorageMethods.isEmailInStorage(email)) {
						StorageMethods.addEmail(email);
					}
					if (!StorageMethods.isUserInStorage(username)) {
						User getU = new User(username, name, email, password, checkData.get("created_at").getAsString(), checkData.get("updated_at").getAsString());

						StorageMethods.setUser(getU);
					}
					StorageMethods.throwEmailAlreadyExists(request,response);
				}
			}else{
				System.out.println("FInalm Register because no methods found");
				RegisterUser registerUser=new RegisterUser();
				RegisterThread r1=new RegisterThread(registerUser,username,name,email,password,request,response);
				r1.start();
				try{
					r1.join();
				}catch (Exception e){
					throw e;
				}
			}
			out.flush();



		}catch(Exception e)
		{
			StorageMethods.throwUnknownError(request,response);
		}
	}

}
