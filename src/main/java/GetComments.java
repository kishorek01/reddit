import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;

/**
 * Servlet implementation class GetComments
 */
public class GetComments extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetComments() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=SessionManager.validateSession(request,response);
		if(username!=null) {
			String postid = request.getParameter("postid");
			String parentcomment = request.getParameter("parentcomment");
			try {
				StorageMethods.getCommentsByPostID(postid, request, response);
			} catch (Exception e) {
				StorageMethods.throwUnknownError(request, response);
			}
		}else {
			StorageMethods.throwSessionExpired(request,response);
		}

	}


}
