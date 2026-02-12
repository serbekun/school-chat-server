package school.chat.services;

import java.util.List;

import school.chat.core.Chat;
import school.chat.core.Chat.ChatMessage;
import school.chat.core.Whitelist;
import school.chat.core.Users;
import school.chat.core.VerificationText;
import school.chat.core.ProhibitedLogins;
import school.chat.services.dto.SendChatMessageRequest;
import school.chat.services.dto.SendChatMessageResponse;
import school.chat.services.dto.GetChatMessagesRequest;
import school.chat.services.dto.SendVerificationTextRequest;
import school.chat.services.dto.SendVerificationTextResponse;

/**
 * Service layer for chat related operations. Handles all business logic that
 * was previously embedded in the HTTP handlers. This keeps the handlers thin
 * and focused purely on HTTP plumbing.
 */
public class ChatService {

    private final Chat chat;
    private final Whitelist whitelist;
    private final Users users;
    private final VerificationText verificationText;
    private final ProhibitedLogins prohibitedLogins;

    public ChatService(Chat chat, Users users, Whitelist whitelist,
                       VerificationText verificationText, ProhibitedLogins prohibitedLogins) {
        this.chat = chat;
        this.users = users;
        this.whitelist = whitelist;
        this.verificationText = verificationText;
        this.prohibitedLogins = prohibitedLogins;
    }

    /**
     * Handle sending a chat message.
     */
    public SendChatMessageResponse sendChatMessage(SendChatMessageRequest req, boolean whitelistMode) {
        // Whitelist check (if enabled)
        if (whitelistMode) {
            if (!whitelist.ContainsId(req.id)) {
                return new SendChatMessageResponse(false, "Access denied: not in whitelist", 1);
            }
        }

        // Prohibited check
        if (!prohibitedLogins.CheckLogin(req.login)) {
            return new SendChatMessageResponse(false, "Account is banned", 4);
        }
        // Basic auth
        if (!users.checkUser(req.login, req.password)) {
            return new SendChatMessageResponse(false, "Incorrect login or password", 2);
        }
        boolean userIsVerified = false;
        if (users.isUserVerified(req.login)) {
            if (!users.checkUserVerified(req.login, req.password, req.id)) {
                return new SendChatMessageResponse(false,
                        "Incorrect login or password and error authenticate", 2);
            }
            userIsVerified = true;
        }
        // Append message
        chat.AppendToChat(req.login, req.text, userIsVerified);
        return new SendChatMessageResponse(true, "Message sent successfully", 0);
    }

    /**
     * Appends a new message to the chat
     */
    public void AppendToChat(String login, String message, boolean verified) {
        chat.AppendToChat(login, message, verified);
    }

    /**
     * Removes a message by its index (0-based). Throws exception if index is invalid.
     */
    public void RemoveMessageByNumber(int num) {
        chat.RemoveMessageByNumber(num);
    }

    /**
     * Clears all messages from cache and file
     */
    public void ClearChat() {
        chat.ClearChat();
    }

    /**
     * Get chat messages with whitelist check.
     */
    public String GetChatMessages(GetChatMessagesRequest req, boolean whitelistMode) {
        if (whitelistMode) {
            if (!whitelist.ContainsId(req.id)) {
                return null; // Signal that whitelist check failed
            }
        }
        return chat.GetChatMessageJsonString();
    }
    
    /**
     * Returns an unmodifiable view of the current chat messages
     */
    public List<ChatMessage> GetChatMessages() {
        return List.copyOf(chat.GetChatMessage()); // immutable copy for safety
    }

    /**
     * Send verification text for a user.
     */
    public SendVerificationTextResponse sendVerificationText(SendVerificationTextRequest req, boolean whitelistMode) {
        // Whitelist check (if enabled)
        if (whitelistMode) {
            if (!whitelist.ContainsId(req.id)) {
                return new SendVerificationTextResponse(false, "Access denied: not in whitelist", 1);
            }
        }

        // Check if user is banned
        if (!prohibitedLogins.CheckLogin(req.login)) {
            return new SendVerificationTextResponse(false, "Account is banned", 4);
        }

        // Authenticate user
        if (!users.checkUser(req.login, req.password)) {
            return new SendVerificationTextResponse(false, "Incorrect login or password", 2);
        }

        verificationText.AppendVerificationText(req.id, req.login, req.text);
        return new SendVerificationTextResponse(true, null, 0);
    }

}

