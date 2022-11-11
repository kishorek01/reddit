package reddit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Storage {
    protected static ConcurrentHashMap<String, User> users=new ConcurrentHashMap<>();
    protected static CopyOnWriteArrayList<String> registeredEmails=new CopyOnWriteArrayList<>();
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
