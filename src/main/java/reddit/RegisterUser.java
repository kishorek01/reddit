package reddit;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class RegisterUser {
    public synchronized void register(String username, String name, String email, String password, HttpServletRequest request, HttpServletResponse response) throws SQLException ,IOException{
        System.out.println("Creating Using Thread");
        PrintWriter out=response.getWriter();
            JsonObject result = Database.registerUser(username, name, email, password);
            System.out.println("New User Created Successfully");
            out.print(result);
            out.flush();
    }
}
