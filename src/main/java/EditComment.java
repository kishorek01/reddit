import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;

/**
 * Servlet implementation class PostComments
 */
public class EditComment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Gson gson = new Gson();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComment() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username=SessionManager.validateSession(request,response);
        if(username!=null) {
            String comment = request.getParameter("comment");
            String commentId = request.getParameter("commentid");
            String postId = request.getParameter("postid");

            try {
//			JsonObject commentData=Database.postComments(username,comment,postid);
                StorageMethods.editComments(username, comment, commentId, postId, request, response);
            } catch (Exception e) {
                StorageMethods.throwUnknownError(request, response);
            }
        }else{
            StorageMethods.throwSessionExpired(request,response);
        }


    }

}
