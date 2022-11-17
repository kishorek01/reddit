package reddit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
        response.setContentType("application/json");
        res.add("data",likeData);
        res.addProperty("liked", true);
        res.addProperty("message","Post Liked/Unliked Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
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
    public static void addLikeToPosts(String likeId,String postId){
        if(!posts.get(postId).likes.contains(likeId)){
            posts.get(postId).likes.add(likeId);
        }
    }
    public static void addLikeToComment(String likeId,String commentId){
        if(!comments.get(commentId).likes.contains(likeId)){
            comments.get(commentId).likes.add(likeId);
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
        response.setContentType("application/json");
        JsonObject finalResponse=new JsonObject();
        HttpSession session = request.getSession();
        session.setAttribute("username",user.username);
        session.setMaxInactiveInterval(10*60);
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
        finalResponse.addProperty("code", 200);
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
        response.setContentType("application/json");
        res.addProperty("Commentget", true);
        res.addProperty("message", "Comment get Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }

    public static void getMyPosts(String username,HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("Getting from in Memory");
        JsonArray postData;
        JsonObject res=new JsonObject();
        response.setContentType("application/json");
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
        res.addProperty("postget", true);
        res.addProperty("message", "Post get Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }

    public static synchronized void getAllPosts(HttpServletRequest request,HttpServletResponse response) throws Exception{
        System.out.println("Getting from in Memory");
        JsonArray postData;
        JsonObject finalResponse=new JsonObject();
        ArrayList<Posts> arr=new ArrayList<>();
        Set<String> myPosts=posts.keySet();
        response.setContentType("application/json");
        for(String i: myPosts){
            arr.add(posts.get(i));
//            if(commentsByPostId.containsKey(posts.get(i).postid) && commentsByPostId.get(posts.get(i).postid).size()>0) {
//                Set<String> keys=commentsByPostId.get(posts.get(i).postid).keySet();
//                System.out.println("This id has comments " +posts.get(i).postid);
//                commentData.add(posts.get(i).postid, new Gson().toJsonTree(commentsByPostId.get(posts.get(i).postid)).getAsJsonObject());
//            }
            //Commented FOr ONly SHowing Post in Homepage no Comments;
        }
        postData = new Gson().toJsonTree(arr).getAsJsonArray();
        finalResponse.addProperty("postget", true);
        finalResponse.addProperty("message", "Post get Successful");
        finalResponse.add("data", postData);
        finalResponse.addProperty("code", 200);
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }


    public static void getLikesFromMemory(String contentId,HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("Getting from in Memory");
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        response.setContentType("application/json");
        JsonObject likeData;
        if(likesByContentId.containsKey(contentId) && likesByContentId.get(contentId).size()>0) {
            System.out.println("This id has likes " +contentId);
            likeData=new Gson().toJsonTree(likesByContentId.get(contentId)).getAsJsonObject();
        }else {
            likeData=new JsonObject();
        }
        res.add("data",likeData);
        res.addProperty("postget", true);
        res.addProperty("message", "Post get Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
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
        response.setContentType("application/json");
        JsonObject finalResponse=new JsonObject();
        finalResponse.addProperty("code", 501);
        res.addProperty("message","Unknown Error");
        finalResponse.add("data", res);
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }

    public static void throwSessionExpired(HttpServletRequest request,HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        finalResponse.addProperty("code", 500);
        res.addProperty("message","Session Expired");
        finalResponse.add("data", res);
        PrintWriter out=response.getWriter();
        out.print(finalResponse);
        out.flush();
    }


    public static void addLikesToPosts(String contentId,Boolean status,String username,HttpServletRequest request,HttpServletResponse response) throws IOException {
        String LikeId=Database.RandomIDLikeGenerator(username);
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        Like newLike=new Like(LikeId,contentId,username,status,false);
        likes.put(LikeId,newLike);
        response.setContentType("application/json");
        if(!likesByContentId.containsKey(contentId)){
            likesByContentId.put(contentId,new ConcurrentHashMap<>());
        }
        if(!likesByContentId.get(contentId).containsKey(newLike.likeid)){
            likesByContentId.get(contentId).put(newLike.likeid,newLike);
        }
        newLikeQueue.add(LikeId);
        res.add("data",new Gson().toJsonTree(likes.get(LikeId),Like.class));
        PrintWriter out=response.getWriter();
        res.addProperty("Edited", true);
        res.addProperty("message", "Post Edited Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();

    }
    public static void addPostLikes(String likeId,String contentId,Like like) throws IOException {
        likes.put(likeId,like);
        if(!likesByContentId.containsKey(contentId)){
            likesByContentId.put(contentId,new ConcurrentHashMap<>());
        }
        if(!likesByContentId.get(contentId).containsKey(like.likeid)){
            likesByContentId.get(contentId).put(like.likeid,like);
        }


    }
    public static void addLikesToComments(String contentId,Boolean status,String username,HttpServletRequest request,HttpServletResponse response) throws IOException {
        String LikeId=Database.RandomIDLikeGenerator(username);
        JsonObject res=new JsonObject();
        response.setContentType("application/json");
        JsonObject finalResponse=new JsonObject();
        Like newLike=new Like(LikeId,contentId,username,status,true);
        likes.put(LikeId,newLike);
        if(!likesByContentId.containsKey(contentId)){
            likesByContentId.put(contentId,new ConcurrentHashMap<>());
        }
        if(!likesByContentId.get(contentId).containsKey(newLike.likeid)){
            likesByContentId.get(contentId).put(newLike.likeid,newLike);
        }
        newLikeQueue.add(LikeId);
        res.add("data",new Gson().toJsonTree(likes.get(LikeId),Like.class));
        PrintWriter out=response.getWriter();
        res.addProperty("Edited", true);
        res.addProperty("message", "Post Edited Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static void addCommentLikes(String likeId,String contentId,Like like) throws IOException {
        likes.put(likeId,like);
        if(!likesByContentId.containsKey(contentId)){
            likesByContentId.put(contentId,new ConcurrentHashMap<>());
        }
        if(!likesByContentId.get(contentId).containsKey(like.likeid)){
            likesByContentId.get(contentId).put(like.likeid,like);
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
        response.setContentType("application/json");
        res.addProperty("commented", true);
        res.addProperty("message","Commented Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static void createPost(String username,String content,HttpServletRequest request, HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        String postId=Database.RandomIDGenerator(username);
        LocalDateTime myDateObj = LocalDateTime.now();
        String date=myDateObj.toString();
        date=date.replace('T',' ');
        date=date+"+05:30";
        Posts postData=new Posts(postId,content,username, date,date);
        posts.put(postId,postData);
        res.add("data",new Gson().toJsonTree(postData));
        users.get(username).myPosts.add(postId);
        Storage.newPostQueue.add(postId);
        PrintWriter out=response.getWriter();
        res.addProperty("posted", true);
        response.setContentType("application/json");
        res.addProperty("message", "Posted Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static synchronized void removePostFromCommentsAndLikes(String postId) throws Exception{
        Set<String> comK=comments.keySet();
        for(String key:comK){
            if(comments.get(key).postid.equals(postId)){
                comments.remove(key);
            }
        }
        Set<String> likK=likes.keySet();
        for(String key:likK){
            if(likes.get(key).contentid.equals(postId)){
                likes.remove(key);
            }
        }
    }

    public static synchronized void deletePost(String username,String postId,HttpServletRequest request,HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        response.setContentType("application/json");
        JsonObject finalResponse=new JsonObject();
        try{
            Database.deletePost(postId);
            users.get(username).myPosts.remove(postId);
            posts.remove(postId);
            if(commentsByPostId.containsKey(postId)) {
                commentsByPostId.remove(postId);
            }
            if(likesByContentId.containsKey(postId)) {
                likesByContentId.remove(postId);
            }
            removePostFromCommentsAndLikes(postId);
            res.addProperty("Deleted", true);
            res.addProperty("message", "Post Deleted Successfully");
            finalResponse.add("data", res);
            finalResponse.addProperty("code", 200);
        }catch (Exception e){
            e.printStackTrace();
            res.addProperty("Deleted", false);
            res.addProperty("message", "Unable to Delete Post");
            finalResponse.add("data", res);
            finalResponse.addProperty("code", 300);
        }
        PrintWriter out=response.getWriter();

        out.print(finalResponse);
        out.flush();
    }
    public static void editPost(String postId,String content,HttpServletRequest request, HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        response.setContentType("application/json");
        posts.get(postId).content=content;
        res.add("data",new Gson().toJsonTree(posts.get(postId),Posts.class));
        Storage.editPostQueue.add(postId);
        PrintWriter out=response.getWriter();
        res.addProperty("Edited", true);
        res.addProperty("message", "Post Edited Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static void editComments(String username,String comment,String commentId,String postId,HttpServletRequest request,HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        response.setContentType("application/json");
        JsonObject finalResponse=new JsonObject();
        comments.get(commentId).comment=comment;
        commentsByPostId.get(postId).get(commentId).comment=comment;
        res.add("data",new Gson().toJsonTree(comments.get(commentId),Comments.class));
        Storage.editCommentQueue.add(commentId);
        PrintWriter out=response.getWriter();
        res.addProperty("Edited", true);
        res.addProperty("message", "Post Edited Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }
    public static void deleteComments(String username,String commentId,String postId,HttpServletRequest request,HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        response.setContentType("application/json");
        comments.get(commentId).comment="";
        commentsByPostId.get(postId).get(commentId).comment="";
        res.add("data",new Gson().toJsonTree(comments.get(commentId),Comments.class));
        Storage.editCommentQueue.add(commentId);
        PrintWriter out=response.getWriter();
        res.addProperty("Edited", true);
        res.addProperty("message", "Post Edited Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static void editLikes(String likeId,Boolean status,String contentId,HttpServletRequest request,HttpServletResponse response) throws IOException{
        JsonObject res=new JsonObject();
        response.setContentType("application/json");
        JsonObject finalResponse=new JsonObject();
        likes.get(likeId).status=status;
        System.out.println("Edit Like here "+likes.get(likeId).status);
        likesByContentId.get(contentId).get(likeId).status=status;
        res.add("data",new Gson().toJsonTree(likes.get(likeId),Like.class));
        Storage.editLikeQueue.add(likeId);
        PrintWriter out=response.getWriter();
        res.addProperty("Liked", true);
        res.addProperty("message", "Liked Edited Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
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
                    subsqlposts=subsqlposts+"'"+postsData.content+"',";
                    subsqlposts=subsqlposts+"'"+postsData.created_at+"',";
                    subsqlposts=subsqlposts+"'"+postsData.updated_at+"'";
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

    public static synchronized void createNewLike() throws Exception{
        String likeSql="";
        String likeKey = newLikeQueue.poll();
        while (likeKey!=null){
            String subsqlposts="";

            if (likeKey != null) {

                System.out.println("Createing Like " + likeKey);
                if(likeSql.contains(")")){
                    likeSql=likeSql+",";
                }
                subsqlposts=subsqlposts+"(";
                Like likeData = likes.get(likeKey);
                subsqlposts=subsqlposts+"'"+likeKey+"',";
                subsqlposts=subsqlposts+"'"+likeData.contentid+"',";
                subsqlposts=subsqlposts+"'"+likeData.username+"',";
                subsqlposts=subsqlposts+""+likeData.status+",";
                subsqlposts=subsqlposts+""+likeData.comment+"";
                subsqlposts=subsqlposts+")";
                likeSql=likeSql+subsqlposts;

            }

            likeKey = newLikeQueue.poll();
        }
        System.out.println(likeSql);

        if(likeSql.length()>0){
            Database.postBatchLikes(likeSql);
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

    public static synchronized void getMessages(String conversationId,HttpServletRequest request,HttpServletResponse response) throws Exception{
        JsonObject res=new JsonObject();
        response.setContentType("application/json");
        JsonObject finalResponse=new JsonObject();
        JsonObject message=messages.get(conversationId);
        System.out.println("Getting message conversation : "+conversationId);
        res.add("data",message);
        System.out.println("Message Size  : "+message.size());
        PrintWriter out=response.getWriter();
        res.addProperty("Messages", true);
        res.addProperty("message", "Messages Obtained Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static synchronized void getConversations(String username,HttpServletRequest request,HttpServletResponse response) throws Exception{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        HashMap<String,String> message=users.get(username).messages;
        JsonArray conversations=new JsonArray();
        if(message.size()!=0){
            Set<String> keys=message.keySet();
            JsonObject obj=new JsonObject();
            for(String key: keys){
                obj.addProperty("username",key);
                obj.addProperty("conversationid",message.get(key));
                conversations.add(obj);
            }
        }
        System.out.println("Getting message conversation : "+conversations);
        res.add("data",conversations);
        System.out.println("Message Size  : "+conversations.size());
        response.setContentType("application/json");
        PrintWriter out=response.getWriter();
        res.addProperty("Conversations", true);
        res.addProperty("conversations", "Conversations Obtained Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static synchronized void postMessage(String conversationId,String username,String content,HttpServletRequest request, HttpServletResponse response) throws Exception{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        JsonObject newMessage=new JsonObject();
        newMessage.addProperty("username",username);
        response.setContentType("application/json");
        newMessage.addProperty("message",content);
        messages.get(conversationId).add("M"+(messages.get(conversationId).size()+1),newMessage);
        System.out.println("Getting message conversation : "+conversationId);
        MessagesQueue.add(conversationId);
        res.add("data",messages.get(conversationId));
        System.out.println("Message Size  : "+messages.get(conversationId).size());
        PrintWriter out=response.getWriter();
        res.addProperty("Messages", true);
        res.addProperty("message", "Messages Added Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static synchronized void DeleteMessage(String conversationId,String username,String messageId,HttpServletRequest request, HttpServletResponse response) throws Exception{
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        JsonObject newMessage=new JsonObject();
        newMessage.addProperty("username",username);
        response.setContentType("application/json");
        newMessage.addProperty("message","");
        messages.get(conversationId).add(messageId,newMessage);
        System.out.println("Delete message conversation : "+conversationId);
        MessagesQueue.add(conversationId);
        res.add("data",messages.get(conversationId));
        System.out.println("Message Size  : "+messages.get(conversationId).size());
        PrintWriter out=response.getWriter();
        res.addProperty("Messages", true);
        res.addProperty("message", "Messages Added Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }

    public static synchronized void addMessagesToMemory(String conversationId, JsonElement message) throws Exception{
        messages.put(conversationId,message.getAsJsonObject());
    }

    public static synchronized Boolean isConversationPresent(String user1,String user2) throws Exception{
        return users.get(user1).messages.containsKey(user2);
    }

    public static synchronized void getConversation(String user1,String user2,HttpServletRequest request,HttpServletResponse response) throws Exception{
        getMessages(user1,request,response);
    }

    public static synchronized void createConversation(String user1,String user2,HttpServletRequest request,HttpServletResponse response) throws Exception{
        response.setContentType("application/json");
        String conversationId=Database.RandomConversationID(user1,user2);
        if(!users.containsKey(user1)){
            Database.loginUser(user1);
        }
        if(!users.containsKey(user2)){
            Database.loginUser(user2);
        }
        System.out.println("All Users Logined");
        Conversation newConversation=new Conversation(user1,user2,conversationId);
        System.out.println("New Conversation Id + "+conversationId);
        addConversationstoUsers(user1,user2,conversationId);
//        Storage.newConversationQueue.add(conversationId);
        try {
            Storage.conversations.put(conversationId, newConversation);
            JsonObject newMessage = new JsonObject();
            Database.createConversation(user1, user2, conversationId);
            messages.put(conversationId, newMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonObject res=new JsonObject();
        JsonObject finalResponse=new JsonObject();
        System.out.println("Creating message conversation : "+conversationId);
        MessagesQueue.add(conversationId);
        res.add("data",messages.get(conversationId));
        System.out.println("Message Size  : "+messages.get(conversationId).size());
        PrintWriter out=response.getWriter();
        res.addProperty("Messages", true);
        res.addProperty("message", "Messages Created Successful");
        finalResponse.add("data", res);
        finalResponse.addProperty("code", 200);
        out.print(finalResponse);
        out.flush();
    }



    public static synchronized void addConversationstoUsers(String user1,String user2,String conversationId) throws Exception{
        if(!users.containsKey(user1)){
            Database.loginUser(user1);
        }
        if(!users.containsKey(user2)){
            Database.loginUser(user2);
        }
        users.get(user1).messages.put(user2,conversationId);
        users.get(user2).messages.put(user1,conversationId);
    }


    public static synchronized void UpdateLikeDB() throws Exception{
        String editLikeSql="";
        String likeKey = editLikeQueue.poll();
        while (likeKey!=null){
            System.out.println("Updating Comment " + likeKey);
//            editLikeSql=editLikeSql+"SET ";
            Like likeData = likes.get(likeKey);
//            editLikeSql=editLikeSql+"comment='"+commentData.comment+"' ";
//            editLikeSql=editLikeSql+"where likeid='"+likeKey+"'";
//            System.out.println(editLikeSql);
//            if(editLikeSql.length()>0){
                Database.updateLikes(likeKey,likeData.status,likeData.username);
                System.out.println("Data Added Successfully");
//            }
            likeKey = editLikeQueue.poll();
        }
    }


    public static synchronized void updateMessagesDB() throws Exception{
        String messagesSql="";
        String messageKey=MessagesQueue.poll();
        while (messageKey!=null){
            JsonObject message=messages.get(messageKey).getAsJsonObject();
            Database.updateMessage(messageKey,message);
            messageKey=MessagesQueue.poll();
        }
    }



}
