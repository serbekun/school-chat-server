package school.chat.services.dto;

/**
 * Response DTO for sending a chat message.
 */
public class SendChatMessageResponse {
    public boolean success;
    public String message;
    public int exitCode;

    public SendChatMessageResponse() {
    }

    public SendChatMessageResponse(boolean success, String message, int exitCode) {
        this.success = success;
        this.message = message;
        this.exitCode = exitCode;
    }
}
