package reddit;

public class Message {
    String message;
    String username;

    Message(String createdBy, String message) {
        this.message = message;
        this.username = createdBy;
    }
}
