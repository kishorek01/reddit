package reddit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class RegisterThread extends Thread{
    RegisterUser registerUser;
    String username;
    String email;
    String password;
    String name;
    HttpServletRequest request;
    HttpServletResponse response;
    public RegisterThread(RegisterUser registerUser, String username, String name, String email, String password,HttpServletRequest request,HttpServletResponse response){
        this.username=username;
        this.name=name;
        this.registerUser=registerUser;
        this.email=email;
        this.password=password;
        this.request=request;
        this.response=response;
    }

    public void run(){
        try {
            registerUser.register(username,name,email,password,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
