package school.chat.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Chat {

    private final String chatMessagesSaveFilePath;
    private final List<ChatMessage> messages; // in-memory cache
    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Inner class representing a single message
    public static class ChatMessage {
        private String user;
        private String time; // stored as formatted string
        private boolean verified;
        private String message;

        // Required no-arg constructor for Jackson
        public ChatMessage() {}

        public ChatMessage(String user, String message, boolean verified) {
            this.user = user;
            this.message = message;
            this.verified = verified;
            this.time = LocalDateTime.now().format(TIME_FORMATTER);
        }

        // Getters and setters
        public String getUser() { return user; }
        public void setUser(String user) { this.user = user; }

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public boolean isVerified() { return verified; }
        public void setVerified(boolean verified) { this.verified = verified; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public Chat(String chatMessagesSaveFilePath) {
        this.chatMessagesSaveFilePath = chatMessagesSaveFilePath;
        this.messages = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        // Pretty print JSON for readability
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        LoadFromFile();
    }

    @FunctionalInterface
    public interface ChatAppend {
        void append(String login, String message, boolean verified);
    }

    /**
     * Appends a new message to the chat
     */
    public synchronized void AppendToChat(String login, String message, boolean verified) {
        if (login == null || login.isBlank() || message == null) {
            System.err.println("AppendToChat called with invalid parameters");
            return;
        }
        ChatMessage newMessage = new ChatMessage(login, message, verified);
        messages.add(newMessage);
        SaveToFile();
    }

    @FunctionalInterface
    public interface RemoveMessage {
        void Remove(int number);
    }

    /**
     * Removes a message by its index (0-based). Throws exception if index is invalid.
     */
    public synchronized void RemoveMessageByNumber(int number) {
        if (number < 0 || number >= messages.size()) {
            System.err.println("Out of bounds messages index: " + number);
            return;
        }
        messages.remove(number);
        SaveToFile();
    }

    /**
     * Returns an unmodifiable view of the current chat messages
     */
    public synchronized List<ChatMessage> GetChatMessage() {
        return List.copyOf(messages); // immutable copy for safety
    }

    /**
     * Return json String of messages
     */
    public synchronized String GetChatMessageJsonString() {
        try {
            String result = objectMapper.writeValueAsString(messages);
            return result;
        } catch (IOException e) {
            System.err.println("Error make json string from messages object" + e.getMessage());
            return "[]";
        }
    }

    @FunctionalInterface
    public interface ClearChat {
        void Clear();
    }

    /**
     * Clears all messages from cache and file
     */
    public synchronized void ClearChat() {
        messages.clear();
        SaveToFile(); // ensure file is also cleared
    }

    /**
     * Loads messages from the JSON file into memory
     */
    public void LoadFromFile() {
        File file = new File(chatMessagesSaveFilePath);
        if (!file.exists() || file.length() == 0) {
            messages.clear();
            return;
        }

        try {
            List<ChatMessage> loaded = objectMapper.readValue(file, new TypeReference<List<ChatMessage>>() {});
            messages.clear();
            messages.addAll(loaded);
        } catch (IOException e) {
            System.err.println("FAILED TO LOAD FILE: " + chatMessagesSaveFilePath);
            e.printStackTrace();
            messages.clear();
        }
    }

    /**
     * Saves current in-memory messages to the JSON file
     */
    public void SaveToFile() {
        try {
            // Ensure parent directories exist
            File file = new File(chatMessagesSaveFilePath);
            if (file.getParentFile() != null) {
                Files.createDirectories(file.getParentFile().toPath());
            }

            objectMapper.writeValue(file, messages);
        } catch (IOException e) {
            System.err.println("Failed to save chat messages to file: " + e.getMessage());
        }
    }
}