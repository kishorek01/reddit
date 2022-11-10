

import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import com.google.gson.*;
import java.sql.*;
/**
 * Servlet implementation class Register
 */
import reddit.Database;
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    Connection connection=null;
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
		String username = request.getParameter("username").toString().toLowerCase();
        String email = request.getParameter("email").toString();
        String password = request.getParameter("password").toString();
        String name = request.getParameter("name").toString();
		PrintWriter out=response.getWriter();  
		try {
			connection=Database.initializeDatabase();
			if(connection!=null) {
			System.out.println("Database Connected Successfully");
			String sql="Select * from users where username='"+username+"' or email='"+email+"';";
			Statement stmt=connection.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			int c=0;
			JsonObject res=new JsonObject();
			JsonObject finalresponse=new JsonObject();
			if(rs.next()) {
				c++;
				if(username.equalsIgnoreCase(rs.getString("username"))) {
					res.addProperty("message","Username Already Exists");
					res.addProperty( "created", false);
					finalresponse.add("data", res);
					finalresponse.addProperty("code",201);
					System.out.println("Username Already Exists");
					out.print(finalresponse);
				}else if(email.compareTo(rs.getString("email"))==0) {
					res.addProperty("message","Email Already Exists");
					res.addProperty( "created", false);
					finalresponse.add("data", res);
					finalresponse.addProperty("code",201);
					System.out.println("Email Already Exists");
					out.print(finalresponse);
				}
				out.flush();
			}
			if(c==0) {
				String createsql="insert into users(username,email,password,name) values('"+username+"','"+email+"','"+password+"','"+name+"') RETURNING *;";
				PreparedStatement cstmt = connection.prepareStatement(createsql);
				ResultSet r=cstmt.executeQuery();
				finalresponse.addProperty("code", 200);
				res.addProperty("message", "User Created Successfully");
				JsonObject userdata=new JsonObject();
				if(r.next()) {
					userdata.addProperty("username",r.getString("username"));
					userdata.addProperty("name", r.getString("name"));
					userdata.addProperty("password", r.getString("password"));
					userdata.addProperty("email", r.getString("email"));
					userdata.addProperty("created_at", r.getString("created_at"));
					
				}
				res.add("data",userdata);
				finalresponse.add("data", res);
				out.print(finalresponse);
				out.flush();
		        System.out.println("New User Created Successfully");
		        out.flush();
			}
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
		}catch(Exception e)
		{
			JsonObject res=new JsonObject();
			JsonObject finalresponse=new JsonObject();
			finalresponse.addProperty("code", 501);
			res.addProperty("message","Unknown Error");
			finalresponse.add("data", res);
			out.print(finalresponse);
			e.printStackTrace();
			out.flush();

		}
		  
		  
		  
	}

}
