package reddit;

import java.util.ArrayList;

public class Posts {
    public String content;
    public String postid;
    public String created_by;
    public String created_at;
    public String updated_at;
    public int countLike;
    public ArrayList<String> comments;
    public ArrayList<String> likes;

    Posts(String postId, String content, String created_by,String created_at,String updated_at,int countLike) {
        this.postid = postId;
        this.content = content;
        this.created_by = created_by;
        this.created_at=created_at;
        this.countLike=countLike;
        this.updated_at=updated_at;
        this.comments = new ArrayList<>();
        this.likes=new ArrayList<>();
    }
}
