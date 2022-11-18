package reddit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    public String name;
    public String email;
    public String password;
    public String username;
    public String created_at;
    public String updated_at;
    public ArrayList<String> myPosts;
    public HashMap<String,String> messages;
    public User(String username,String name,String email,String password,String created_at,String updated_at){
        this.name=name;
        this.email=email;
        this.password=password;
        this.username=username.toLowerCase();
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.myPosts=new ArrayList<>();
        this.messages=new HashMap<>();
    }
    public User(){
        LocalDateTime myDateObj = LocalDateTime.now();
        String date=myDateObj.toString();
        date=date.replace('T',' ');
        date=date+"+05:30";
        this.name="";
        this.email="";
        this.password="";
        this.created_at=date;
        this.updated_at=date;
        this.username="";
        this.myPosts=new ArrayList<>();
        this.messages=new HashMap<>();
    }
}
