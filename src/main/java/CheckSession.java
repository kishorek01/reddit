import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class Login
 */

public class CheckSession extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckSession() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session=request.getSession(false);
            PrintWriter out=response.getWriter();
            JsonObject obj=new JsonObject();
            response.setContentType("application/json");
            obj.addProperty("session", session != null);
            out.println(obj);
            out.flush();
        }catch (Exception e){
            StorageMethods.throwUnknownError(request,response);
        }
    }

}
