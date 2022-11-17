package reddit;

import java.time.LocalDateTime;

public class Like {
    public String likeid;
    public String contentid;
    public String username;
    public Boolean status;
    public Boolean comment;
    public String created_at;
    public String updated_at;
    public Like(String likeId,String contentId,String username,Boolean status,Boolean comment){
        LocalDateTime myDateObj = LocalDateTime.now();
        String date=myDateObj.toString();
        date=date.replace('T',' ');
        date=date+"+05:30";
        this.likeid=likeId;
        this.contentid=contentId;
        this.username=username;
        this.status=status;
        this.comment=comment;
        this.created_at= date;
        this.updated_at=date;
    }
    public Like(String likeId,String contentId,String username,Boolean status,Boolean comment,String created_at,String updated_at){
        this.likeid=likeId;
        this.contentid=contentId;
        this.username=username;
        this.status=status;
        this.comment=comment;
        this.created_at= created_at;
        this.updated_at=updated_at;

    }
}
