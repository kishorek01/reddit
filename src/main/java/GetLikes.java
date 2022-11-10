

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
 * Servlet implementation class GetLikes
 */
public class GetLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLikes() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection=null;
		int contentid = Integer.parseInt(request.getParameter("contentid"));
		Boolean comment=Boolean.parseBoolean(request.getParameter("comment"));
        response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
				String sql="select * from likes where contentid='"+contentid+"' and comment="+comment+";";
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(sql);
				JsonObject res=new JsonObject();
				JsonObject finalresponse=new JsonObject();
					res.add("data",convertToJSON(rs));
					res.addProperty("posts", true);
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
                if(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase().equalsIgnoreCase("likeid") || resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase().equalsIgnoreCase("contentid")) {
                	obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getInt(i+1));
                }else if(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase().equalsIgnoreCase("status") || resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase().equalsIgnoreCase("comment")) {
                	obj.addProperty(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getString(i+1).compareTo("t")==0);
                }
            }
          jsonArray.add(obj);
        }
        return jsonArray;
    }

}
