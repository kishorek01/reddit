package reddit;
import java.util.ArrayList;
import java.util.HashMap;

public class Storage {
    protected static HashMap<String, User> users=new HashMap<>();
    protected static ArrayList<String> registeredEmails=new ArrayList<>();
    public static User getUser(String userName){
        if(users.containsKey(userName)){
            return users.get(userName);
        }else{
            return new User();
        }
    }

    public static Boolean isUserInStorage(String userName){
        return users.containsKey(userName);
    }

    public static Boolean isEmailInStorage(String email){
        return registeredEmails.contains(email);
    }

    public static void setUser(User user){
        users.put(user.username,user);
    }
    public static void addEmail(String email){
        registeredEmails.add(email);
    }
}
