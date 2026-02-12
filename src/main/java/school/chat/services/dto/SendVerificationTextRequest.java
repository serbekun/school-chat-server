package school.chat.services.dto;

/**
 * Request DTO for sending verification text.
 */
public class SendVerificationTextRequest {
    public String login;
    public String password;
    public String id;
    public String text;

    public SendVerificationTextRequest() {
    }

    public SendVerificationTextRequest(String login, String password, String id, String text) {
        this.login = login;
        this.password = password;
        this.id = id;
        this.text = text;
    }
}
