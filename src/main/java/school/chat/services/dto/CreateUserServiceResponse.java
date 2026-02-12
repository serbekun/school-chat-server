package school.chat.services.dto;

/**
 * Response DTO for user creation operation.
 */
public class CreateUserServiceResponse {

    public boolean success;
    public String message;
    public int exitCode;

    public CreateUserServiceResponse() {
    }

    public CreateUserServiceResponse(boolean success, String message, int exitCode) {
        this.success = success;
        this.message = message;
        this.exitCode = exitCode;
    }

}