package school.chat.CommandLine.commands;

import java.util.HashMap;
import java.util.Map;

public class Help {
    
    private final Map<String, CommandInfo> commandRegistry = new HashMap<>();
    
    public Help() {
        // Register all available commands with their descriptions
        registerCommand("stop", "stop server application", 
            "Usage: stop\nStops the server application gracefully.");
        
        registerCommand("help", "show help info", 
            "Usage: help [command]\n" +
            "Options:\n" +
            "  --commands, -c  - show all available commands\n" +
            "  --all, -a      - show detailed help for all commands\n" +
            "  <command>      - shows information about a specific command");
            
        // Add more commands as they become available
        registerCommand("chat", "manage chat operations",
            "Usage: chat [option]\n" +
            "Options:\n" +
            "  --show         - show all chat messages\n" +
            "  --say <msg>    - send a message as server\n" +
            "  --remove <#>   - remove message by number\n" +
            "  --clear        - clear all chat messages");

        registerCommand("users", "manage users accounts",
            "Usage users [option]\n" +
            "Options:\n" +
            "  --verify <login>    - set user verify by login\n" +
            "  --un-verify <login> - set user un verify by login\n" + 
            "  --show              - show all users accounts\n" +
            "  --remove <login>    - remove user by login\n" +
            "  --clear             - remove all users\n" +
            "  --ban <login>       - ban user by login\n" +
            "  --un-ban <login>    - un ban user by login");

        registerCommand("server", "manage server",
            "Usage server [option]\n" +
            "Options:\n" +
            "--start  - start server\n" +
            "--stop   - stop server\n" +
            "--port   - show server port\n" +
            "--status - show server status");
    }
    
    private void registerCommand(String name, String brief, String detailed) {
        commandRegistry.put(name, new CommandInfo(name, brief, detailed));
    }
    
    public void Run(String[] args) {
        if (args.length < 1) {
            System.out.println("No command specified.\n");
            showUsage();
            return;
        }

        switch (args[0]) {
            case "--commands":
            case "-c":
                showCommandList();
                break;
                
            case "--all":
            case "-a":
                showAllCommands();
                break;
                
            default:
                // Check if it's a registered command
                if (commandRegistry.containsKey(args[0])) {
                    showCommandHelp(args[0]);
                } else {
                    System.out.printf("Unknown command: '%s'\n", args[0]);
                    showUsage();
                }
                break;
        }
    }
    
    private void showUsage() {
        System.out.println("Usage: help [option] | [command]");
        System.out.println("Options:");
        System.out.println("  --commands, -c  - show all available commands");
        System.out.println("  --all, -a      - show detailed help for all commands");
        System.out.println("  <command>      - shows information about a specific command");
        System.out.println("\nExamples:");
        System.out.println("  help --commands   # List all commands");
        System.out.println("  help stop         # Show help for 'stop' command");
        System.out.println("  help -a           # Show all detailed help");
    }
    
    private void showCommandList() {
        System.out.println("Available commands:");
        System.out.println("────────────────────");
        commandRegistry.forEach((name, info) -> {
            System.out.printf("%-10s - %s\n", name, info.brief);
        });
        System.out.println("\nUse 'help <command>' for more information about a specific command.");
    }
    
    private void showAllCommands() {
        System.out.println("Detailed help for all commands:");
        System.out.println("───────────────────────────────");
        
        commandRegistry.forEach((name, info) -> {
            System.out.println(info.detailed);
            if (!name.equals(getLastCommandName())) {
                Line();
            }
        });
    }
    
    private void showCommandHelp(String commandName) {
        CommandInfo info = commandRegistry.get(commandName);
        if (info != null) {
            System.out.println(info.detailed);
        } else {
            System.out.printf("No help available for command: %s\n", commandName);
        }
    }
    
    private String getLastCommandName() {
        return commandRegistry.keySet().stream()
            .reduce((first, second) -> second)
            .orElse("");
    }
    
    private void Line() {
        System.out.println("\n" + "─".repeat(40) + "\n");
    }
    
    // Helper class to store command information
    private static class CommandInfo {
        String name;
        String brief;
        String detailed;
        
        CommandInfo(String name, String brief, String detailed) {
            this.name = name;
            this.brief = brief;
            this.detailed = detailed;
        }
    }
    
    // Public method to allow other command classes to register themselves
    public void registerExternalCommand(String name, String brief, String detailed) {
        registerCommand(name, brief, detailed);
    }
}