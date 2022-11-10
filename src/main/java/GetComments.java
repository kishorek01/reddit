

import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.Database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
		int postid = Integer.parseInt(request.getParameter("postid"));
		String parentcomment=request.getParameter("parentcomment");
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
				String sql;
				if(parentcomment.equalsIgnoreCase("null")) {
					sql="select * from comments where postid="+postid+";";
				}else {
					int cid=Integer.parseInt(parentcomment);
					sql="select * from comments where postid="+postid+" and parentcomment="+cid+";";
				}
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(sql);
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
					res.add("data",convertToJSON(rs));
					res.addProperty("comments", true);
					res.addProperty("message","Posts Get Successful");
					finalresponse.add("data", res);
					finalresponse.addProperty("Code", 200);
					out.print(finalresponse);
					out.flush();
				
			}else {
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
				finalresponse.addProperty("code", 501);
				res.addProperty("message", "Database Connection Error");
				finalresponse.add("data", res);
				System.out.println("Database Connected Error");
				out.print(finalresponse);
				out.flush();
			}
			
		}catch(Exception e) {
			JsonObject res=new JsonObject();
			JsonObject finalresponse=new JsonObject();
			finalresponse.addProperty("code",501);
			res.addProperty("message","Unknown Error");
			finalresponse.add("data", res);
			out.print(finalresponse);
			e.printStackTrace();
			out.flush();
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
                }else if(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase().equalsIgnoreCase("childcomments")) {
                	String s=resultSet.getString(i+1);
                	s=s.replace('{', '[');
                	s=s.replace('}',']');
                	obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), s);
                }
            }
          jsonArray.add(obj);
        }
        return jsonArray;
    }

}
