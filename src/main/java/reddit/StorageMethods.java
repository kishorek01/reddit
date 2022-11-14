package reddit;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class StorageMethods extends Storage{
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
    public static void addPost(String postId,Posts post) {
        posts.put(postId, post);
    }
    public static void addComment(String commentId,Comments comment){
        comments.put(commentId,comment);
    }
    public static Boolean isPostinPosts(String postId){
        System.out.println("Adding POst"+postId+ " to inmemory");
        return posts.containsKey(postId);
    }

    public static Boolean isCommentinComments(String commentID){
        System.out.println("Checkimg for comment in Comments");
        return comments.containsKey(commentID);
    }
    public static void addPostToUsers(String username,String postId){
        if(!users.get(username).myPosts.contains(postId)) {
            System.out.println("Adding POst"+postId+ " to inmemory of users");
            users.get(username).myPosts.add(postId);
        }
    }
    public static void addCommentToPosts(String commentId,String postId){
        if(!posts.get(postId).comments.contains(commentId)){
            posts.get(postId).comments.add(commentId);
        }
    }
    public static void throwEmailAlreadyExists(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Getting From In Memory");
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        res.addProperty("message","Email Already Exists");
        res.addProperty( "created", false);
        finalResponse.add("data", res);
        finalResponse.addProperty("code",201);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println("Email Already Exists");
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }

    public static void throwUsernameAlreadyExists(HttpServletRequest request,HttpServletResponse response) throws IOException{
        System.out.println("Getting From In Memory");
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        res.addProperty("message","Username Already Exists");
        res.addProperty( "created", false);
        finalResponse.add("data", res);
        finalResponse.addProperty("code",201);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println("Username Already Exists");
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }

    public static void throwUnknownError(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        finalResponse.addProperty("code", 501);
        res.addProperty("message","Unknown Error");
        finalResponse.add("data", res);
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }
}
