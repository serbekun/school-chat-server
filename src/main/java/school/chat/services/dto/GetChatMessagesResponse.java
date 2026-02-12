package school.chat.services.dto;

/**
 * Response DTO for getting chat messages.
 */
public class GetChatMessagesResponse {
    public boolean success;
    public String message;
    public int exitCode;

    public GetChatMessagesResponse() {
    }

    public GetChatMessagesResponse(boolean success, String message, int exitCode) {
        this.success = success;
        this.message = message;
        this.exitCode = exitCode;
    }
}
