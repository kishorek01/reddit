import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
		String username = request.getParameter("username").toString().toLowerCase();
		String email = request.getParameter("email").toString();
		String password = request.getParameter("password").toString();
		String name = request.getParameter("name").toString();
		if(StorageMethods.isEmailInStorage(email)){
			System.out.println("Getting From In Memory");
			JsonObject res=new JsonObject();
			JsonObject finalResponse=new JsonObject();
			res.addProperty("message","Email Already Exists");
			res.addProperty( "created", false);
			finalResponse.add("data", res);
			finalResponse.addProperty("code",201);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			System.out.println("Email Already Exists");
			PrintWriter out=response.getWriter();
			out.print(finalResponse);
			out.flush();
		} else if (!StorageMethods.isUserInStorage(username)) {
			createFromDB(request, response, username, email, password, name);
		}else{
			User user = StorageMethods.getUser(username);
			System.out.println("Getting From In Memory");
			JsonObject res=new JsonObject();
			JsonObject finalResponse=new JsonObject();
			res.addProperty("message","Username Already Exists");
			res.addProperty( "created", false);
			finalResponse.add("data", res);
			finalResponse.addProperty("code",201);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			System.out.println("Username Already Exists");
			PrintWriter out=response.getWriter();
			out.print(finalResponse);
			out.flush();
		}
	}


	protected void createFromDB(HttpServletRequest request, HttpServletResponse response,String username,String email,String password, String name) throws ServletException, IOException{
		System.out.println("Creating in DB");
		Connection connection=null;
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		try {


			connection= Database.initializeDatabase();
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
						if(!StorageMethods.isUserInStorage(rs.getString("username"))) {
							User getU = new User(rs.getString("username"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("created_at"), rs.getString("updated_at"));
							StorageMethods.setUser(getU);
						}
						if(!StorageMethods.isEmailInStorage(rs.getString("email"))) {
							StorageMethods.addEmail(rs.getString("email"));
						}
						out.print(finalresponse);
					}else if(email.compareTo(rs.getString("email"))==0) {
						if(!StorageMethods.isEmailInStorage(rs.getString("email"))) {
							StorageMethods.addEmail(rs.getString("email"));
						}
						if(!StorageMethods.isUserInStorage(rs.getString("username"))) {
							User getU = new User(rs.getString("username"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("created_at"), rs.getString("updated_at"));

							StorageMethods.setUser(getU);
						}
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

					RegisterUser newUser=new RegisterUser();
					RegisterThread r1=new RegisterThread(newUser,username,name,email,password,request,response);
					r1.start();
					try{
						r1.join();
					}catch (Exception e){
						e.printStackTrace();
					}




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
