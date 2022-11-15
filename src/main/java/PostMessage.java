import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

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
        Connection connection=null;
        String conversationId = request.getParameter("conversationid");
        String username = request.getParameter("username");
        String content=request.getParameter("message");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out=response.getWriter();
        try {

            StorageMethods.postMessage(conversationId,username,content,request,response);
//				String sql="update posts set content='"+content+"' where postid='"+postid+"' RETURNING *;";
        }catch(Exception e) {
            StorageMethods.throwUnknownError(request,response);
        }

    }

}
