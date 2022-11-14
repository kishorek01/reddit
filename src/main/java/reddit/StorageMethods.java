package reddit;

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
}
