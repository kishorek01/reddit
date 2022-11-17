import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
				String username1 = request.getParameter("username").toString().toLowerCase();
				String user2 = request.getParameter("user2").toString().toLowerCase();
				PrintWriter out = response.getWriter();
				try {


					if (StorageMethods.isConversationPresent(username1, user2)) {
						System.out.println("Getting Data From in memory");
						StorageMethods.getConversation(username1, user2, request, response);
					} else {
						System.out.println("Creating Data to memory");
						StorageMethods.createConversation(username1, user2, request, response);
					}

				} catch (Exception e) {
					StorageMethods.throwUnknownError(request, response);

				}
			}else{
				StorageMethods.throwSessionExpired(request,response);
			}
		  
		  

	}

}
