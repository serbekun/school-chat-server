package school.chat.services.dto;

/**
 * Request DTO for getting chat messages.
 */
public class GetChatMessagesRequest {
    public String id;

    public GetChatMessagesRequest() {
    }

    public GetChatMessagesRequest(String id) {
        this.id = id;
    }
}
