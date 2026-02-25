package school.chat.http_server.handlers.ApiHandlers;

import io.javalin.http.Context;
import school.chat.http_server.handlers.ApiHandlers.dto.GetChatMessagesNotInWhitelistResponse;
import school.chat.http_server.handlers.ApiHandlers.dto.GetChatMessagesRequest;
import school.chat.services.ChatService;

/**
 * <p>Handles the GET request to the <code>/v0/api/messages</code> endpoint.</p>
 *
 * <p>Returns the current chat state as a JSON array of messages.</p>
 *
 * <p><strong>Query parameter:</strong> <code>?id=a1b2c3d4e5f6g7h8i9j0</code></p>
 *
 * <p><strong>Possible responses:</strong></p>
 * <ul>
 *   <li>Whitelist mode enabled and ID not in whitelist:
 *     <pre><code>{
 *       "success": false,
 *       "message": "Not in whitelist.",
 *       "exitCode": 1
 *     }</code></pre>
 *   </li>
 *
 *   <li>Success: direct JSON array of chat messages (no success/exitCode wrapper)</li>
 * </ul>
 *
 * <p>The exact format of the successful response depends on the implementation of <code>chat.GetChatMessageJsonString()</code>.</p>
 */
public class GetChatMessages {
    public void Run(Context ctx, ChatService chatService, boolean whitelistMode) {

    GetChatMessagesRequest req = new GetChatMessagesRequest();
    req.id = ctx.queryParam("id");

    school.chat.services.dto.GetChatMessagesRequest serviceReq = new school.chat.services.dto.GetChatMessagesRequest(req.id);
    String result = chatService.GetChatMessages(serviceReq, whitelistMode);
    
    if (result == null) {
        // Whitelist check failed
        ctx.json(new GetChatMessagesNotInWhitelistResponse(false, "Not in whitelist.", 1));
        return;
    }

    ctx.result(result);
    }
}
