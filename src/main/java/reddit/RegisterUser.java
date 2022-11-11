package reddit;

import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterUser {
    public synchronized void register(String username, String name, String email, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out=response.getWriter();
        try {
            System.out.println("Creating Using Thread");
            JsonObject res=new JsonObject();
            JsonObject finalresponse=new JsonObject();
            Connection connection= Database.initializeDatabase();
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
                if(!Storage.isUserInStorage(username)) {
                    User getU = new User(r.getString("username"), r.getString("name"), r.getString("email"), r.getString("password"), r.getString("created_at"), r.getString("updated_at"));
                    Storage.setUser(getU);
                }
            }
            res.add("data",userdata);
            finalresponse.add("data", res);
            out.print(finalresponse);
            out.flush();
            System.out.println("New User Created Successfully");
            out.flush();

        }catch (Exception e){
            JsonObject res=new JsonObject();
            JsonObject finalresponse=new JsonObject();
            finalresponse.addProperty("code", 501);
            res.addProperty("message", "Database Connection Error");
            finalresponse.add("data", res);
            System.out.println("Database Connected Error");
            out.print(finalresponse);
            out.flush();

        }

    }
}
