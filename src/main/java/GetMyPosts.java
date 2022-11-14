import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.Database;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class GetMyPosts
 */
public class GetMyPosts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMyPosts() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
		String username = request.getParameter("username").toLowerCase();
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		JsonObject res=new JsonObject();
		JsonObject finalResponse=new JsonObject();
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
				String sql="select * from posts where created_by='"+username+"';";
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(sql);

					res.add("data",convertToJSON(rs));
					res.addProperty("posts", true);
					res.addProperty("message","Posts Get Successful");
				finalResponse.add("data", res);
				finalResponse.addProperty("Code", 200);
					out.print(finalResponse);
					out.flush();
				
			}else {
				finalResponse.addProperty("code", 501);
				res.addProperty("message", "Database Connection Error");
				finalResponse.add("data", res);
				System.out.println("Database Connected Error");
				out.print(finalResponse);
				out.flush();
			}
			
		}catch(Exception e) {
			StorageMethods.throwUnknownError(request,response);
		}

	}
	public static JsonArray convertToJSON(ResultSet resultSet)
            throws Exception {
		JsonArray jsonArray = new JsonArray();
        while (resultSet.next()) {
            int total_columns = resultSet.getMetaData().getColumnCount();
            JsonObject obj = new JsonObject();
            for (int i = 0; i < total_columns; i++) {
                obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getString(i+1));
                if(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase().equalsIgnoreCase("postid")) {
                	obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getInt(i+1));
                }
            }
          jsonArray.add(obj);
        }
        return jsonArray;
    }

}
