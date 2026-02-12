package school.chat.http_server.handlers.ApiHandlers.dto;

public class SendChatMessageRequest {
    public String login;
    public String password;
    public String id;
    public String text;

    public SendChatMessageRequest() {}

    public SendChatMessageRequest(String login, String password, String id, String text) {
        this.login = login;
        this.password = password;
        this.id = id;
        this.text = text;
    }

}
