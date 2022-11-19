import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class CommentLikes
 */
public class CommentLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentLikes() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=SessionManager.validateSession(request,response);
		if(username!=null) {
			String commentid = request.getParameter("commentid");
			String postid = request.getParameter("postid");
			boolean status = Boolean.parseBoolean(request.getParameter("status"));
			PrintWriter out = response.getWriter();
				try {
					if (StorageMethods.isCommentInComments(commentid)) {
						StorageMethods.addLikesToComments(postid,commentid, status, username, request, response);
					} else {
						StorageMethods.addCommentMemory(commentid);
						StorageMethods.addLikesToComments(postid,commentid, status, username, request, response);
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
