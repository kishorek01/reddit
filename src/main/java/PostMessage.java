import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;

/**
 * Servlet implementation class EditPost
 */
public class PostMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostMessage() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=SessionManager.validateSession(request,response);
        if(username!=null) {
            String conversationId = request.getParameter("conversationid");
            String content = request.getParameter("message");
            try {
                StorageMethods.postMessage(conversationId, username, content, request, response);
            } catch (Exception e) {
                StorageMethods.throwUnknownError(request, response);
            }
        }else{
            StorageMethods.throwSessionExpired(request,response);
        }

    }

}
