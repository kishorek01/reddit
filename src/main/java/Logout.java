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

public class Logout extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session=request.getSession(false);
            if(session!=null){
                System.out.println("Closing Session For a user");
                session.invalidate();
            }
            JsonObject finalResponse = new JsonObject();
            finalResponse.addProperty("code", 200);
            finalResponse.addProperty("logout", true);
            PrintWriter out=response.getWriter();
            finalResponse.addProperty("message", "Logged Out Successful");
            out.print(finalResponse);
            out.flush();
        }catch (Exception e){
            StorageMethods.throwUnknownError(request,response);
        }
    }

}
