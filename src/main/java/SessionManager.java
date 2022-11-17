import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionManager {
    public static String validateSession(HttpServletRequest request, HttpServletResponse response){
        HttpSession session=request.getSession(false);
        if(session!=null){
            String name=(String)session.getAttribute("username");
            System.out.println("Session For User "+name);
            return name;
        }
        else{
            System.out.println("Please login first");
            return null;
        }
    }
}
