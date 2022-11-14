import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;

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
		String username = request.getParameter("username").toLowerCase();
        String contentid= request.getParameter("contentid");
        boolean status= Boolean.parseBoolean(request.getParameter("status"));
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
				if(status){
					if(StorageMethods.isCommentInComments(contentid)){
						StorageMethods.addLikesToComments(contentid,status,username);
					}else{
						StorageMethods.addCommentMemory(contentid);
						StorageMethods.addLikesToComments(contentid,status,username);

					}

				}else{
					if(StorageMethods.isPostinPosts(contentid)){
						StorageMethods.addLikesToPosts(contentid,status,username);
					}else{
						StorageMethods.addPostMemory(contentid);
						StorageMethods.addLikesToPosts(contentid,status,username);
					}
				}
			
		}catch(Exception e) {
			StorageMethods.throwUnknownError(request,response);
		}

	}

}