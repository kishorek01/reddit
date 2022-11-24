import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.Database;
import reddit.StorageMethods;

import java.io.IOException;

/**
 * Servlet implementation class EditPost
 */
public class GetPost extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPost() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=SessionManager.validateSession(request,response);
        if(username!=null) {
            String postId = request.getParameter("postid");
            try {
                String sortType = request.getParameter("sort_type");
                String parentcomment = request.getParameter("parentcomment");
                if(sortType==null){
                    sortType="new";
                }

//                StorageMethods.getPost(postId, request, response);
                Database.getSortedPost(postId,sortType,parentcomment,request,response);
            } catch (Exception e) {
                e.printStackTrace();
                StorageMethods.throwUnknownError(request, response);
            }
        }else {
            StorageMethods.throwSessionExpired(request,response);
        }

    }

}
