package reddit;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Comments  {
    public String comment;
    public String username;
    public String commentid;
    public String postid;
    public String created_at;
    public String updated_at;
    public String parentcomment;
    public int like;
    public int dislike;
    public ArrayList<String> childcomments;
    public ArrayList<String> likes;

    public Comments(String commentid,String comment, String commentedBy, String postId) {
        this.comment = comment;
        this.commentid=commentid;
        LocalDateTime myDateObj = LocalDateTime.now();
        String date=myDateObj.toString();
        date=date.replace('T',' ');
        date=date+"+05:30";
        this.created_at=date;
        this.updated_at=date;
        this.username = commentedBy;
        this.postid = postId;
        this.like=0;
        this.dislike=0;
        this.childcomments = new ArrayList<>();
        this.parentcomment=null;
        this.likes=new ArrayList<>();
    }
    public Comments(String commentid, String comment, String commentedBy, String postId, String parentcomment, ArrayList<String> childcomments) {
        this.comment = comment;
        this.commentid=commentid;
        LocalDateTime myDateObj = LocalDateTime.now();
        String date=myDateObj.toString();
        date=date.replace('T',' ');
        date=date+"+05:30";
        this.username = commentedBy;
        this.postid = postId;
        this.childcomments = childcomments;
        this.like=0;
        this.dislike=0;
        this.parentcomment=parentcomment;
        this.created_at=date;
        this.likes=new ArrayList<>();
        this.updated_at=date;
    }
    public Comments(String commentid, String comment, String commentedBy, String postId, String parentcomment, ArrayList<String> childcomments,String created_at, String updated_at) {
        this.comment = comment;
        this.commentid=commentid;
        this.username = commentedBy;
        this.like=0;
        this.dislike=0;
        this.postid = postId;
        this.childcomments = childcomments;
        this.parentcomment=parentcomment;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.likes=new ArrayList<>();
    }
}
