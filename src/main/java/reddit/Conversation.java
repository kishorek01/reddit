package reddit;

public class Conversation {
    public String user1;
    public String user2;
    public String conversationid;

    public String created_at;
    public String updated_at;
    public Conversation(String user1,String user2,String conversationid,String created_at,String updated_at){
        this.user1=user1;
        this.user2=user2;
        this.conversationid=conversationid;
        this.created_at=created_at;
        this.updated_at=updated_at;
    }
}
