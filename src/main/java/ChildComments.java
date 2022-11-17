import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;

/**
 * Servlet implementation class ChildComments
 */
public class ChildComments extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChildComments() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=SessionManager.validateSession(request,response);
		if(username!=null) {
			String comment = request.getParameter("comment");
			String parentcomment = request.getParameter("parentcomment");
			String postid = request.getParameter("postid");
			try {
				StorageMethods.postComments(username, comment, postid, "Comment", parentcomment, request, response);
			} catch (Exception e) {
				StorageMethods.throwUnknownError(request, response);
			}
		}else {
			StorageMethods.throwSessionExpired(request,response);
		}

		

	}

}
