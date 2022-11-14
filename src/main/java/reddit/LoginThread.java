package reddit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginThread extends Thread{
    String username;
    String password;
    LoginUser loginUser;
    HttpServletResponse response;
    HttpServletRequest request;
    public LoginThread(LoginUser loginUser, String username, String password, HttpServletRequest request, HttpServletResponse response){
        this.loginUser=loginUser;
        this.username=username;
        this.password=password;
        this.request=request;
        this.response=response;
    }

    public void run(){
        try {
            loginUser.getPosts(username);
            loginUser.getMessages(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
