package reddit;

import com.google.gson.JsonObject;

public class LoginUser {
    public synchronized void getPosts(String username) throws Exception {
        System.out.println("Getting Posts Using Thread");
        JsonObject result=Database.getMyPosts(username);
        System.out.println("Got all the post for user "+ username+" and stored it in memory");

    }
    public synchronized void getMessages(String username){

    }
}
