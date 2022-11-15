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
public class EditLike extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Gson gson = new Gson();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditLike() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String likeid= request.getParameter("likeid");
        String contentId= request.getParameter("contentid");
        boolean status= Boolean.parseBoolean(request.getParameter("status"));

        try {
            System.out.println("Edit Like Here");
//			JsonObject commentData=Database.postComments(username,comment,postid);
            StorageMethods.editLikes(likeid,status,contentId,request,response);
        }catch(Exception e) {
            StorageMethods.throwUnknownError(request,response);
        }


    }

}
