import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
/**
 * Servlet implementation class CreatePost
 */
public class CreatePost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreatePost() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
		String username = request.getParameter("username").toLowerCase();
        String content = request.getParameter("content");
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
				StorageMethods.createPost(username,content,request,response);
//				String sql="insert into posts(created_by,content) values('"+username+"','"+content+"') RETURNING *;";
//				Statement stmt=connection.createStatement();
//				ResultSet rs=stmt.executeQuery(sql);


		}catch(Exception e) {
			StorageMethods.throwUnknownError(request,response);
		}
	}
}
