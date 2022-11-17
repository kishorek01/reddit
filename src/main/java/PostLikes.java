import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;

/**
 * Servlet implementation class AddLikes
 */
public class PostLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostLikes() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=SessionManager.validateSession(request,response);
		if(username!=null) {
			String contentid = request.getParameter("contentid");
			boolean status = Boolean.parseBoolean(request.getParameter("status"));
			try {
				if (StorageMethods.isPostinPosts(contentid)) {
					StorageMethods.addLikesToPosts(contentid, status, username, request, response);
				} else {
					StorageMethods.addPostMemory(contentid);
					StorageMethods.addLikesToPosts(contentid, status, username, request, response);
				}
			} catch (Exception e) {
				StorageMethods.throwUnknownError(request, response);
			}
		}else{
			StorageMethods.throwSessionExpired(request,response);
		}

	}

}