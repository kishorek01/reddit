import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * Servlet implementation class ChildComments
 */
public class ChildComments extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChildComments() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection connection=null;
		String username = request.getParameter("username").toLowerCase();
        String comment = request.getParameter("comment");
        String parentcomment = request.getParameter("parentcomment");
        String postid = request.getParameter("postid");
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {

//					String append="update comments set childcomments=array_append(childcomments, "+rs.getInt("commentid")+") where commentid="+parentcomment+" and postid="+postid+" RETURNING *;";
			StorageMethods.postComments(username,comment,postid,"Comment",parentcomment,request,response);


				

			
		}catch(Exception e) {
			StorageMethods.throwUnknownError(request,response);
		}

		

	}

}
