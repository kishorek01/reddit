package reddit;

import java.util.ArrayList;

public class Comments  {
    public String comment;
    public String username;
    public String commentid;
    public String postid;
    public String created_at;
    public String updated_at;
    public String parentcomment;
    public ArrayList<String> childcomments;
    public ArrayList<String> likes;

    public Comments(String commentid,String comment, String commentedBy, String postId) {
        this.comment = comment;
        this.commentid=commentid;
        this.username = commentedBy;
        this.postid = postId;
        this.childcomments = new ArrayList<>();
        this.parentcomment=null;
        this.likes=new ArrayList<>();
    }
    public Comments(String commentid, String comment, String commentedBy, String postId, String parentcomment, ArrayList childcomments) {
        this.comment = comment;
        this.commentid=commentid;
        this.username = commentedBy;
        this.postid = postId;
        this.childcomments = childcomments;
        this.parentcomment=parentcomment;
        this.created_at="";
        this.likes=new ArrayList<>();
        this.updated_at="";
    }
    public Comments(String commentid, String comment, String commentedBy, String postId, String parentcomment, ArrayList childcomments,String created_at, String updated_at) {
        this.comment = comment;
        this.commentid=commentid;
        this.username = commentedBy;
        this.postid = postId;
        this.childcomments = childcomments;
        this.parentcomment=parentcomment;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.likes=new ArrayList<>();
    }
}
