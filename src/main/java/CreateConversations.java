import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.Database;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class CreateConversations
 */
public class CreateConversations extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateConversations() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=SessionManager.validateSession(request,response);
		if(username!=null) {
				String user2 = request.getParameter("user2").toString().toLowerCase();
				PrintWriter out = response.getWriter();
				try {
					if(StorageMethods.isUserInStorage(user2)) {
						if (StorageMethods.isConversationPresent(username, user2)) {
							System.out.println("Getting Data From in memory");
							StorageMethods.getConversation(username, user2, request, response);
						} else {
							System.out.println("Creating Data to memory");
							StorageMethods.createConversation(username, user2, request, response);
						}
					}else{
						JsonObject userData=Database.loginUser(user2);
						if(userData.has("username")){
							if (StorageMethods.isConversationPresent(username, user2)) {
								System.out.println("Getting Data From in memory");
								StorageMethods.getConversation(username, user2, request, response);
							} else {
								System.out.println("Creating Data to memory");
								StorageMethods.createConversation(username, user2, request, response);
							}
						}else{
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							JsonObject data = new JsonObject();
							JsonObject finalResponse = new JsonObject();
							data.addProperty("login", false);
							data.addProperty("message", "User Not Found");
							finalResponse.add("data", data);
							finalResponse.addProperty("code", 202);
							out.print(finalResponse);
							out.flush();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					StorageMethods.throwUnknownError(request, response);

				}
			}else{
				StorageMethods.throwSessionExpired(request,response);
			}
		  
		  

	}

}
