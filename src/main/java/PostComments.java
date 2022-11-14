import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.Database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * Servlet implementation class PostComments
 */
public class PostComments extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson = new Gson();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostComments() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Connection connection=null;
		String username = request.getParameter("username").toLowerCase();
        String comment = request.getParameter("comment");
        String postid = request.getParameter("postid");
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		JsonObject res=new JsonObject();
		JsonObject finalResponse=new JsonObject();
		try {
			JsonObject commentData=Database.postComments(username,comment,postid);

					res.add("data",commentData);
					res.addProperty("commented", true);
					res.addProperty("message","Commented Successful");
					finalResponse.add("data", res);
					finalResponse.addProperty("Code", 200);
					out.print(finalResponse);
					out.flush();

				

			
		}catch(Exception e) {
			finalResponse.addProperty("code",501);
			res.addProperty("message","Unknown Error");
			finalResponse.add("data", res);
			out.print(finalResponse);
			e.printStackTrace();
			out.flush();
		}

		
	}

}
