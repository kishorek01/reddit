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
public class SortPost extends HttpServlet {
    private static final long serialVersionUID = 1L;
    Gson gson = new Gson();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SortPost() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=SessionManager.validateSession(request,response);
        if(username!=null) {
            String sortType = request.getParameter("sort_type");
            if(sortType==null){
                sortType="new";
            }
            try {
                System.out.println("Sorting Post Here");
                StorageMethods.sortAllPosts(sortType,request,response);
            } catch (Exception e) {
                StorageMethods.throwUnknownError(request, response);
            }
        }else {
            StorageMethods.throwSessionExpired(request,response);
        }


    }

}
