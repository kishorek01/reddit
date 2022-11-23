package reddit;

import com.google.gson.JsonObject;

public class LoginUser {
    public void getPosts(String username) throws Exception {
//        System.out.println("Getting Posts Using Thread");
//        JsonObject res=Database.getAllPosts();
        JsonObject result=Database.getMyPosts(username);
        result=Database.getAllPostsExcept(username);
//        System.out.println("Got all the post for user "+ username+" and stored it in memory");
//        getMessages(username);
    }
    public void getMessages(String username) throws Exception{
//        System.out.println("Getting Messages Using Thread");
        JsonObject result=Database.getMyMessages(username);
//        System.out.println("Got all the Messages for user "+ username+" and stored it in memory");

    }
}
