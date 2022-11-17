import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;
/**
 * Servlet implementation class CreatePost
 */
public class DeletePost extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeletePost() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=SessionManager.validateSession(request,response);
        if(username!=null) {
            String postId = request.getParameter("postid");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            try {
                StorageMethods.deletePost(username,postId, request, response);

            } catch (Exception e) {
                e.printStackTrace();
                StorageMethods.throwUnknownError(request, response);
            }
        }else{
            StorageMethods.throwSessionExpired(request,response);
        }
    }
}
