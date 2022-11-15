import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reddit.StorageMethods;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * Servlet implementation class CreateConversations
 */
public class CreateConversations extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateConversations() {
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
        String user2 = request.getParameter("user2").toString().toLowerCase();
		PrintWriter out=response.getWriter();  
		try {


			if(StorageMethods.isConversationPresent(username,user2)){
				System.out.println("Getting Data From in memory");
				StorageMethods.getConversation(username,user2,request,response);
			}else{
				System.out.println("Creating Data to memory");
				StorageMethods.createConversation(username,user2,request,response);
			}
//			connection=Database.initializeDatabase();
//			if(connection!=null) {
//			System.out.println("Database Connected Successfully");
//			String sql="Select * from conversations where (user1='"+username+"' or user1='"+user2+"') and (user2='"+username+"' or user2='"+user2+"');";
//			Statement stmt=connection.createStatement();
//			ResultSet rs=stmt.executeQuery(sql);
//			int c=0;
//			JsonObject res=new JsonObject();
//			JsonObject finalresponse=new JsonObject();
//			if(rs.next()) {
//				c++;
//				res.addProperty("message","Conversation Already Exists");
//				JsonObject convdata=new JsonObject();
//				convdata.addProperty("user1", rs.getString("user1"));
//				convdata.addProperty("user2", rs.getString("user2"));
//				convdata.addProperty("conversationid", rs.getInt("conversationid"));
//				convdata.addProperty("created_at", rs.getString("created_at"));
//				res.add("data",convdata);
//				finalresponse.add("data", res);
//				finalresponse.addProperty("code",200);
//				System.out.println("Conversation Already Exists");
//				out.print(finalresponse);
//				out.flush();
//			}
//			if(c==0) {
//				String createsql="insert into conversations(user1,user2) values('"+username+"','"+user2+"') RETURNING *;";
//				PreparedStatement cstmt = connection.prepareStatement(createsql);
//				ResultSet r=cstmt.executeQuery();
//				finalresponse.addProperty("code", 200);
//				res.addProperty("message", "Conversation Created Successfully");
//				JsonObject convdata=new JsonObject();
//				if(r.next()) {
//					convdata.addProperty("user1", r.getString("user1"));
//					convdata.addProperty("user2", r.getString("user2"));
//					convdata.addProperty("conversationid", r.getInt("conversationid"));
//					convdata.addProperty("created_at", r.getString("created_at"));
//
//				}
//				res.add("data",convdata);
//				finalresponse.add("data", res);
//				out.print(finalresponse);
//				out.flush();
//		        System.out.println("Conversation Created Successfully");
//		        out.flush();
//			}
//			}else {
//				JsonObject res=new JsonObject();
//				JsonObject finalresponse=new JsonObject();
//				finalresponse.addProperty("code", 501);
//				res.addProperty("message", "Database Connection Error");
//				finalresponse.add("data", res);
//				System.out.println("Database Connected Error");
//				out.print(finalresponse);
//				out.flush();
//			}
		}catch(Exception e)
		{
			StorageMethods.throwUnknownError(request,response);

		}
		  
		  

	}

}
