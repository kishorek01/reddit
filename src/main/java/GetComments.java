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
		String postid = request.getParameter("postid");
		String parentcomment=request.getParameter("parentcomment");

		try {
//			JsonObject commentData= Database.getCommentsForPosts(postid);
			StorageMethods.getCommentsByPostID(postid,request,response);
//				String sql;
//				if(parentcomment.equalsIgnoreCase("null")) {
//					sql="select * from comments where postid="+postid+";";
//				}else {
//					int cid=Integer.parseInt(parentcomment);
//					sql="select * from comments where postid="+postid+" and parentcomment="+cid+";";
//				}
			
		}catch(Exception e) {
			StorageMethods.throwUnknownError(request,response);

		}

	}


}
