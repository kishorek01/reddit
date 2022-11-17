import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;

/**
 * Servlet implementation class EditPost
 */
public class DeleteMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteMessage() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=SessionManager.validateSession(request,response);
        if(username!=null) {
            String conversationId = request.getParameter("conversationid");
            String messageId = request.getParameter("messageid");
            try {
                StorageMethods.DeleteMessage(conversationId, username, messageId, request, response);
            } catch (Exception e) {
                StorageMethods.throwUnknownError(request, response);
            }
        }else {
            StorageMethods.throwSessionExpired(request,response);
        }

    }

}
