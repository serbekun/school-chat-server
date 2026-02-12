package school.chat.CommandLine.commands;

import java.util.List;
import school.chat.core.Chat.ChatMessage;
import school.chat.services.ChatService;

public class CommandChat {
    
    private ChatService chatService;

    public CommandChat(ChatService chatService) {
        this.chatService = chatService;
    }

    public void Run(String[] arguments) {
        
        if (arguments.length < 1) {
            System.out.println("No argument type 'help chat' for show it");
            return;
        }

        switch (arguments[0]) {
            case "--show":
                Show();
                break;
            case "--say":
                Say(arguments[1]);
                break;
            case "--remove":
                try {
                    int number = Integer.parseInt(arguments[1]);
                    Remove(number);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + arguments[1]);
                }
                break;
            case "--clear":
                Clear();
                break;
            default:
                System.out.println("Unknown argument try 'hell chat' for see available arguments");
                break;
        }
    }

    private void Show() {
        List<ChatMessage> messages = chatService.GetChatMessages();

        if (messages.size() == 0) {
            System.out.println("Chat is empty");
            return;
        }

        for (int i = 0; i  < messages.size(); i++) {
            ChatMessage msg = messages.get(i);
            System.out.printf("┌ %d %s %s %b\n", i, msg.getUser(), msg.getTime(), msg.isVerified());
            System.out.printf("└ %s\n", msg.getMessage());
        }
    }

    private void Say(String text) {
        chatService.AppendToChat("server", text, true);
    }

    private void Remove(int number_of_message) {
        chatService.RemoveMessageByNumber(number_of_message);
    }

    private void Clear() {
        chatService.ClearChat();
    }

}
