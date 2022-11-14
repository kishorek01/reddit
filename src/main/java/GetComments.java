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
 * Servlet implementation class GetComments
 */
public class GetComments extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetComments() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
		String postid = request.getParameter("postid");
		String parentcomment=request.getParameter("parentcomment");
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		JsonObject res=new JsonObject();
		JsonObject finalResponse=new JsonObject();
		try {
			JsonObject commentData= Database.getCommentsForPosts(postid);
//				String sql;
//				if(parentcomment.equalsIgnoreCase("null")) {
//					sql="select * from comments where postid="+postid+";";
//				}else {
//					int cid=Integer.parseInt(parentcomment);
//					sql="select * from comments where postid="+postid+" and parentcomment="+cid+";";
//				}
					res.add("data",commentData);
					res.addProperty("comments", true);
					res.addProperty("message","Posts Get Successful");
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
