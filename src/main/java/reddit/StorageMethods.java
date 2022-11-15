package reddit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }
    public static void getCommentsByPostID(String postId,HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("Getting from in Memory");
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        JsonObject commentData;
            if(commentsByPostId.containsKey(postId) && commentsByPostId.get(postId).size()>0) {
                System.out.println("This id has comments " +postId);
                commentData=new Gson().toJsonTree(commentsByPostId.get(postId)).getAsJsonObject();
        }else {
                commentData=new JsonObject();
            }
//            System.out.println(arr);
//            System.out.println(postData);
        res.add("data",commentData);
        res.addProperty("Comment get", true);
        res.addProperty("message", "Comment get Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("Code", 200);
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }

    public static void getMyPosts(String username,HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("Getting from in Memory");
        JsonArray postData;
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        ArrayList<String> myPosts=users.get(username).myPosts;
        JsonObject commentData=new JsonObject();
        ArrayList<Posts> arr=new ArrayList<>();
        for(String i: myPosts){
            arr.add(posts.get(i));
            if(commentsByPostId.containsKey(posts.get(i).postid) && commentsByPostId.get(posts.get(i).postid).size()>0) {
                Set<String> keys=commentsByPostId.get(posts.get(i).postid).keySet();
                System.out.println("This id has comments " +posts.get(i).postid);
//                System.out.println(new Gson().toJsonTree(commentsByPostId.get(posts.get(i).postid)).getAsJsonObject());
                    commentData.add(posts.get(i).postid, new Gson().toJsonTree(commentsByPostId.get(posts.get(i).postid)).getAsJsonObject());
//                    commentData.add(posts.get(i).postid, new Gson().toJsonTree(commentsByPostId.get(posts.get(i).postid)).getAsJsonObject());
            }
        }
//            System.out.println(arr);
            postData = new Gson().toJsonTree(arr).getAsJsonArray();
//            System.out.println(postData);
            res.add("data",postData);
            res.add("commentData",commentData);
        res.addProperty("post get", true);
        res.addProperty("message", "Post get Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("Code", 200);
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }

    public static void addCommentToCommentByPosts(String postId,Comments comment)
    {
        if(!commentsByPostId.containsKey(postId)){
            commentsByPostId.put(postId,new ConcurrentHashMap<>());
        }
        if(!commentsByPostId.get(postId).containsKey(comment.commentid)){
            commentsByPostId.get(postId).put(comment.commentid,comment);
        }


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

    public static void postComments(String username, String comment, String postID, String idType, String parentComment, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        String commentId=Database.RandomIDGenerator(username,idType);
        if(idType.contains("Post")) {
            Comments commentData = new Comments(commentId, comment, username, postID);
            comments.put(commentId, commentData);
            res.add("data", new Gson().toJsonTree(commentData));
        }else{
            ArrayList<String> child = new ArrayList<>();
            Comments commentData = new Comments(commentId, comment, username, postID,parentComment,child);
            comments.put(commentId, commentData);
            res.add("data", new Gson().toJsonTree(commentData));
        }
        Storage.newCommentQueue.add(commentId);
        if(posts.containsKey(postID)){
            posts.get(postID).comments.add(commentId);
        }else{
            Database.getPostID(postID);
            posts.get(postID).comments.add(commentId);
        }
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

    public static void editPost(String postId,String content,HttpServletRequest request, HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        posts.get(postId).content=content;
        res.add("data",new Gson().toJsonTree(posts.get(postId),Posts.class));
        Storage.editPostQueue.add(postId);
        PrintWriter out=response.getWriter();
        res.addProperty("Edited", true);
        res.addProperty("message", "Post Edited Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("Code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static void editComments(String username,String comment,String commentId,String postId,HttpServletRequest request,HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        comments.get(commentId).comment=comment;
        commentsByPostId.get(postId).get(commentId).comment=comment;
        res.add("data",new Gson().toJsonTree(comments.get(commentId),Comments.class));
        Storage.editCommentQueue.add(commentId);
        PrintWriter out=response.getWriter();
        res.addProperty("Edited", true);
        res.addProperty("message", "Post Edited Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("Code", 200);
        out.print(finalResponse);
        out.flush();
    }


    public static synchronized void updateDBComments() throws Exception {
        String postssql = "";
        String commentsql = "";
        String commentKey = newCommentQueue.poll();
        while (commentKey != null) {
            String subsqlposts = "";
            String subSqlComments = "";
            if (commentKey != null) {

                if (commentKey.contains("post") || commentKey.contains("Post")) {
                    System.out.println("Updating Post Comment " + commentKey);

                    if (postssql.contains(")")) {
                        postssql = postssql + ",";
                    }
                    subsqlposts = subsqlposts + "(";
                    Comments commentData = comments.get(commentKey);
                    subsqlposts = subsqlposts + "'" + commentKey + "',";
                    subsqlposts = subsqlposts + "'" + commentData.username + "',";
                    subsqlposts = subsqlposts + "'" + commentData.comment + "',";
                    subsqlposts = subsqlposts + "'" + commentData.postid + "'";
                    subsqlposts = subsqlposts + ")";
//                    Database.postComments(commentKey, commentData.username, commentData.comment, commentData.postid);
                    postssql = postssql + subsqlposts;
                } else {
                    System.out.println("Updating Comment Comment " + commentKey);
                        if (commentsql.contains(")")) {
                            commentsql = commentsql + ",";
                        }
                        subSqlComments = subSqlComments + "(";
                        Comments commentData = comments.get(commentKey);
                        subSqlComments = subSqlComments + "'" + commentKey + "',";
                        subSqlComments = subSqlComments + "'" + commentData.username + "',";
                        subSqlComments = subSqlComments + "'" + commentData.parentcomment + "',";
                        subSqlComments = subSqlComments + "'" + commentData.comment + "',";
                        subSqlComments = subSqlComments + "'" + commentData.postid + "'";
                        subSqlComments = subSqlComments + ")";
                        Database.appendChildComment(commentData.parentcomment,commentKey,commentData.postid);
//                    Database.postComments(commentKey, commentData.username, commentData.comment, commentData.postid);
                        commentsql = commentsql + subSqlComments;
                }
            }
            commentKey = newCommentQueue.poll();
        }
            System.out.println("Create new post Comment query "+postssql);
        System.out.println("Create new CHild comment query "+commentsql);

            if (postssql.length() > 0) {
                Database.postBatchComments(postssql);
                System.out.println("Post Data Added Successfully");
            }
            if(commentsql.length()>0){
                Database.postChildCommentsBatch(commentsql);
                System.out.println("Comment Data Added Successfully");
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

    public static synchronized void UpdateEditDBPosts() throws Exception{
        String editPostSql="";
        String postKey = editPostQueue.poll();
        while (postKey!=null){

                System.out.println("Updating Post " + postKey);
            editPostSql=editPostSql+"SET ";
                Posts postsData = posts.get(postKey);
            editPostSql=editPostSql+"content='"+postsData.content+"' ";
            editPostSql=editPostSql+"where postid='"+postKey+"'";
            System.out.println(editPostSql);
            if(editPostSql.length()>0){
                Database.updatePosts(editPostSql);
                System.out.println("Data Added Successfully");
            }
            postKey = newPostQueue.poll();
        }
    }

    public static synchronized void updateEditDBComments() throws Exception{
        String editCommentSql="";
        String commentKey = editCommentQueue.poll();
        while (commentKey!=null){

            System.out.println("Updating Comment " + commentKey);
            editCommentSql=editCommentSql+"SET ";
            Comments commentData = comments.get(commentKey);
            editCommentSql=editCommentSql+"comment='"+commentData.comment+"' ";
            editCommentSql=editCommentSql+"where commentid='"+commentKey+"'";
            System.out.println(editCommentSql);
            if(editCommentSql.length()>0){
                Database.updateComment(editCommentSql);
                System.out.println("Data Added Successfully");
            }
            commentKey = newPostQueue.poll();
        }
    }

}
