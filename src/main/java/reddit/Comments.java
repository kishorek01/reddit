package reddit;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;

public class Comments {
    public String comment;
    public String username;
    public String commentid;
    public String postid;
    public String created_at;
    public String updated_at;
    public String parentcomment;
    public JsonArray childcomments;
    public HashMap<String, ArrayList> votes;

    public Comments(String commentid,String comment, String commentedBy, String postId) {
        this.comment = comment;
        this.commentid=commentid;
        this.username = commentedBy;
        this.postid = postId;
        this.childcomments = new JsonArray();
        this.parentcomment=null;
        this.votes = new HashMap();
        this.votes.put("upVotes", new ArrayList());
        this.votes.put("downVotes", new ArrayList());
    }
    public Comments(String commentid, String comment, String commentedBy, String postId, String parentcomment, JsonArray childcomments,String created_at, String updated_at) {
        this.comment = comment;
        this.commentid=commentid;
        this.username = commentedBy;
        this.postid = postId;
        this.childcomments = childcomments;
        this.parentcomment=parentcomment;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.votes = new HashMap();
        this.votes.put("upVotes", new ArrayList());
        this.votes.put("downVotes", new ArrayList());
    }
}
