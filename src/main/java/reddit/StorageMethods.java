package reddit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

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

    public static void postLikes(String contentid,String username,Boolean status,HttpServletRequest request,HttpServletResponse response) throws Exception {
        JsonObject likeData=Database.postLikes(contentid,username,status);
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        PrintWriter out=response.getWriter();
        res.add("data",likeData);
        res.addProperty("liked", true);
        res.addProperty("message","Post Liked/Unliked Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("Code", 200);
        out.print(finalResponse);
        out.flush();

    }

    public static Boolean isCommentInComments(String commentID){
        System.out.println("Checkimg for comment in Comments");
        return comments.containsKey(commentID);
    }
    public static void addPostToUsers(String username,String postId){
        if(!users.get(username).myPosts.contains(postId)) {
            System.out.println("Adding POst"+postId+ " to inmemory of users");
            users.get(username).myPosts.add(postId);
        }
    }
    public static void addPostMemory(String postId) throws Exception {
        Database.getPostID(postId);
    }
    public static void addCommentMemory(String commentId) throws Exception {
        Database.getCommentId(commentId);
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

    public static void throwUser(User user,HttpServletRequest request, HttpServletResponse response) throws Exception{
        JsonObject userData = new JsonObject();
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        userData.addProperty("username", user.username);
        userData.addProperty("name", user.name);
        userData.addProperty("password", user.password);
        userData.addProperty("email", user.email);
        userData.addProperty("created_at", user.created_at);
        userData.addProperty("updated_at", user.updated_at);
        res.add("data", userData);
        res.addProperty("login", true);
        res.addProperty("message", "Login Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("Code", 200);
    }

    public static void throwWrongPassword(HttpServletRequest request,HttpServletResponse response) throws Exception{
        System.out.println("Getting From In Memory");
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        res.addProperty("message","Wrong Password");
        res.addProperty( "login", false);
        finalResponse.add("data", res);
        finalResponse.addProperty("code",203);
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


    public static void addLikesToPosts(String contentId,Boolean status,String username){
        String typeData="downVotes";
        if(status){
            typeData="upVotes";
        }
        if(posts.get(contentId).votes.get(typeData).contains(username)) {
            posts.get(contentId).votes.get(typeData).add(username);
        }
    }
    public static void addLikesToComments(String contentId,Boolean status,String username){
        String typeData="downVotes";
        if(status){
            typeData="upVotes";
        }
        if(comments.get(contentId).votes.get(typeData).contains(username)) {
            posts.get(contentId).votes.get(typeData).add(username);
        }
    }

    public static void postComments(String username,String comment,String postID,HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        String commentId=Database.RandomIDGenerator(username,"Post");
        Comments commentData=new Comments(commentId,comment,username,postID);
        comments.put(commentId,commentData);
        res.add("data",new Gson().toJsonTree(commentData));
        Storage.newCommentQueue.add(commentId);
        PrintWriter out=response.getWriter();
        res.addProperty("commented", true);
        res.addProperty("message","Commented Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("Code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static void createPost(String username,String content,HttpServletRequest request, HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        String postId=Database.RandomIDGenerator(username);
        Posts postData=new Posts(postId,content,username, String.valueOf(new Date().getTime()),String.valueOf(new Date().getTime()));
        posts.put(postId,postData);
        res.add("data",new Gson().toJsonTree(postData));
        users.get(username).myPosts.add(postId);
        Storage.newPostQueue.add(postId);
        PrintWriter out=response.getWriter();
        res.addProperty("posted", true);
        res.addProperty("message", "Posted Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("Code", 200);
        out.print(finalResponse);
        out.flush();
    }


    public static synchronized void updateDBComments() throws Exception {
        String postssql="";
        String commentKey = newCommentQueue.poll();
        while (commentKey!=null){
            String subsqlposts="";

            if (commentKey != null) {

                System.out.println("Updating Comment " + commentKey);
                if (commentKey.contains("post") || commentKey.contains("Post")) {
                    if(postssql.contains(")")){
                        postssql=postssql+",";
                    }
                    subsqlposts=subsqlposts+"(";
                    Comments commentData = comments.get(commentKey);
                    subsqlposts=subsqlposts+"'"+commentKey+"',";
                    subsqlposts=subsqlposts+"'"+commentData.username+"',";
                    subsqlposts=subsqlposts+"'"+commentData.comment+"',";
                    subsqlposts=subsqlposts+"'"+commentData.postid+"'";
                    subsqlposts=subsqlposts+")";
//                    Database.postComments(commentKey, commentData.username, commentData.comment, commentData.postid);
                    postssql=postssql+subsqlposts;
                }
            }

            commentKey = newCommentQueue.poll();
        }
        System.out.println(postssql);

        if(postssql.length()>0){
            Database.postBatchComments(postssql);
            System.out.println("Data Added Successfully");
        }
    }

    public static synchronized void updateDBPosts() throws Exception{
        String postssql="";
        String postKey = newPostQueue.poll();
        while (postKey!=null){
            String subsqlposts="";

            if (postKey != null) {

                System.out.println("Updating Post " + postKey);
                    if(postssql.contains(")")){
                        postssql=postssql+",";
                    }
                    subsqlposts=subsqlposts+"(";
                    Posts postsData = posts.get(postKey);
                    subsqlposts=subsqlposts+"'"+postKey+"',";
                    subsqlposts=subsqlposts+"'"+postsData.created_by+"',";
                    subsqlposts=subsqlposts+"'"+postsData.content+"'";
                    subsqlposts=subsqlposts+")";
//                    Database.postComments(commentKey, commentData.username, commentData.comment, commentData.postid);
                    postssql=postssql+subsqlposts;

            }

            postKey = newPostQueue.poll();
        }
        System.out.println(postssql);

        if(postssql.length()>0){
            Database.postBatchPosts(postssql);
            System.out.println("Data Added Successfully");
        }
    }

}
