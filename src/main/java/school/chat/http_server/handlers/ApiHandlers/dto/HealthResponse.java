package school.chat.http_server.handlers.ApiHandlers.dto;

public class HealthResponse {
    public boolean status;

    public HealthResponse(boolean status) {
        this.status = status;
    }
}