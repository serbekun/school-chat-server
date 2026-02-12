package school.chat.http_server.handlers.ApiHandlers.dto;

public class SendVerificationTextRequest {
    public String id;
    public String login;
    public String text;
    public String password;

    public SendVerificationTextRequest() {}

    public SendVerificationTextRequest(String id, String login, String text, String password) {
        this.id = id;
        this.login = login;
        this.text = text;
        this.password = password;
    }
}
