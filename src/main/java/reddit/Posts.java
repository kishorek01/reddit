package reddit;

import java.util.ArrayList;

public class Posts {
    public String content;
    public String postid;
    public String created_by;
    public String created_at;
    public String updated_at;
    public ArrayList<String> comments;

    Posts(String postId, String content, String created_by,String created_at,String updated_at) {
        this.postid = postId;
        this.content = content;
        this.created_by = created_by;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.comments = new ArrayList();
    }
}
