package reddit;

import java.time.LocalDateTime;

public class Like {
    public String likeid;
    public String postid;
    public String commentid;
    public String username;
    public Boolean status;
    public String created_at;
    public String updated_at;
    public Like(String likeId,String postid,String username,Boolean status,String commentid){
        LocalDateTime myDateObj = LocalDateTime.now();
        String date=myDateObj.toString();
        date=date.replace('T',' ');
        date=date+"+05:30";
        this.likeid=likeId;
        this.postid=postid;
        this.commentid=commentid;
        this.username=username;
        this.status=status;
        this.created_at= date;
        this.updated_at=date;
    }
    public Like(String likeId,String postid,String username,Boolean status,String commentid,String created_at,String updated_at){
        this.likeid=likeId;
        this.postid=postid;
        this.username=username;
        this.status=status;
        this.commentid=commentid;
        this.created_at= created_at;
        this.updated_at=updated_at;

    }
}
