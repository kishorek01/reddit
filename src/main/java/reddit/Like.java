package reddit;

import java.util.Date;

public class Like {
    public String likeid;
    public String contentid;
    public String username;
    public Boolean status;
    public Boolean comment;
    public String created_at;
    public String updated_at;
    public Like(String likeId,String contentId,String username,Boolean status,Boolean comment){
        this.likeid=likeId;
        this.contentid=contentId;
        this.username=username;
        this.status=status;
        this.comment=comment;
        this.created_at= String.valueOf(new Date().getTime());
        this.updated_at=String.valueOf(new Date().getTime());
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
