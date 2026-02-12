package school.chat.http_server.handlers.ApiHandlers.dto;

public class GetChatMessagesNotInWhitelistResponse {
    public boolean success;
    public String message;
    public int exitCode;

    public GetChatMessagesNotInWhitelistResponse(boolean success, String message, int exitCode) {
        this.success = success;
        this.message = message;
        this.exitCode = exitCode;
    }
}
