package school.chat.services;

import java.util.Map;

import school.chat.core.Users;
import school.chat.services.dto.CreateUserServiceResponse;
import school.chat.core.Users.User;

public class UserService {
    
    private Users users;
    private WhitelistService whitelistService;
    private ProhibitedLoginService prohibitedLoginService;

    public UserService(Users users, WhitelistService whitelistService,
        ProhibitedLoginService prohibitedLoginService
    ) {

        this.users = users;
        this.whitelistService = whitelistService;
        this.prohibitedLoginService = prohibitedLoginService;

    }

    /**
     * Creates a new user.
     *
     * @param login The user's login name.
     * @param password The user's password.
     * @param id The user's ID.
     * @param whitelistMode A flag indicating whether whitelist mode is enabled. If true, the user's ID must be in the whitelist.
     * @return A CreateUserServiceResponse object containing the result of the operation.  The response includes a boolean indicating success, a message, and an error code.
     */
    public CreateUserServiceResponse CreateUser(String login, String password, String id, boolean whitelistMode) {
        
        // check in whitelist if whitelist mode is enabled
        if (whitelistMode && !whitelistService.ContainsId(id)) {
            return new CreateUserServiceResponse(false, "Not in whitelist.", 1);
        }
        
        // check prohibited logins
        if (!prohibitedLoginService.CheckLogin(login)) {
            return new CreateUserServiceResponse(false, "This login is prohibited", 5);
        }

        if (users.addUser(login, password, false, 0, id)) {
            return new CreateUserServiceResponse(true, "User created successfully", 0);
        } else {
            return new CreateUserServiceResponse(false, "Error creating User", 3);
        }

    }

    public void VerifyUser(String login) {
        users.VerifyUser(login);
    }

    public void UnVerifyUser(String login) {
        users.UnVerifyUser(login);
    }

    public void Remove(String login) {
        users.RemoveUser(login);
    }

    public void clearUsers() {
        users.clearUsers();
    }

    public void BanUser(String login) {
        users.BanUser(login);
    }

    public void unBanUser(String login) {
        users.unBanUser(login);
    }

    /**
     * 
     * Return users info Read only
     * 
     * @return users Map
     */
    public synchronized Map<String, User> getAllUsers() {
        return users.getAllUsers();
    }

}
