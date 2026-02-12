package school.chat.CommandLine.commands;

import java.util.Map;

import school.chat.services.UserService;
import school.chat.core.Users.User;

public class CommandUsers {
    
    private UserService userService;

    public CommandUsers(UserService users) {
        this.userService = users;
    }

    public void Run(String[] arguments) {
        if (arguments.length < 1) {
            System.out.println("No argument type 'help users' for show it");
            return;
        }

        switch (arguments[0]) {
            case "--verify":
                Verify(arguments[1]);
                break;
            case "--un-verify":
                UnVerify(arguments[1]);
                break;
            case "--show":
                Show();
                break;
            case "--remove":
                Remove(arguments[1]);
                break;
            case "--clear":
                Clear();
                break;
            case "--ban":
                BanUser(arguments[1]);
                break;
            case "--un-ban":
                UnBan(arguments[1]);
                break;
            case "--create":

            default:
                System.out.println("Unknown arguments try 'help users' for see arguments list");
                break;
        }
    }

    private void Show() {
        Map<String, User> all = userService.getAllUsers();

        if (all.isEmpty()) {
            System.out.println("No users");
            return;
        }

        for (Map.Entry<String, User> entry : all.entrySet()) {
            User u = entry.getValue();
            
            System.out.println("──────────────────────");
            System.out.println("login    : " + entry.getKey());
            System.out.println("id       : " + u.id);
            System.out.println("verified : " + u.verified);
            System.out.println("banned   : " + u.verified);
            System.out.println("access   : " + u.degreeOfAccess);

        }
        System.out.println("──────────────────────");
    }

    private void Verify(String login) {
        userService.VerifyUser(login);
    }

    private void UnVerify(String login) {
        userService.UnVerifyUser(login);
    }

    private void Remove(String login) {
        userService.Remove(login);
    }

    private void Clear() {
        userService.clearUsers();
    }

    private void BanUser(String login) {
        userService.BanUser(login);
    }

    private void UnBan(String login) {
        userService.unBanUser(login);
    }
}
