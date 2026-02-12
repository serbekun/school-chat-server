package school.chat.CommandLine;

import java.util.Arrays;
import java.util.Scanner;
import io.javalin.Javalin;

// import commands
import school.chat.CommandLine.commands.*;

// import services
import school.chat.services.ChatService;
import school.chat.services.UserService;

/**
 * class for create command line interface for control server
 */ 
public class CommandLine {
    
    private Javalin svr;

    private ChatService chatService;
    private UserService userService;

    public CommandLine(Javalin svr ,ChatService chatService,
        UserService userService
    ) {
        this.svr = svr;

        this.chatService = chatService;
        this.userService = userService;
    }

    /**
     * @param state state of the server
     */
    public void run() {
        
        Help help = new Help();
        CommandChat commandChat = new CommandChat(chatService);
        CommandUsers commandUsers = new CommandUsers(userService);
        Scanner scanner = new Scanner(System.in);
        
        String input;
        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();
            
            // tokenize by spaces
            String[] tokens = input.split("\\s+"); 
            
            // remove first element because we need pass the argument further
            String[] withoutFirst = Arrays.copyOfRange(tokens, 1, tokens.length); 

            if ("stop".equals(input)) {
                svr.stop();
                break;
            }

            switch (tokens[0]) {
                case "help":
                    help.Run(withoutFirst);
                    break;
                case "chat":
                    commandChat.Run(withoutFirst);
                    break;
                case "users":
                    commandUsers.Run(withoutFirst);
                    break;
                default:
                    System.out.printf("Unknown commands '%s' type 'help --all' for get help\n", tokens[0]);
                    break;
            }
        }
        scanner.close();
    }

}
