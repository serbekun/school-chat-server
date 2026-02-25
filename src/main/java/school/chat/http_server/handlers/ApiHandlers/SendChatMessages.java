package school.chat.http_server.handlers.ApiHandlers;

import io.javalin.http.Context;
import school.chat.http_server.handlers.ApiHandlers.dto.SendChatMessageRequest;
import school.chat.http_server.handlers.ApiHandlers.dto.SendChatMessageResponse;
import school.chat.services.ChatService;

public class SendChatMessages {
    
    /**
     * <p>Handles the POST request to <code>/v0/api/messages</code> endpoint.</p>
     *
     * <p><strong>Request body example:</strong></p>
     * <pre><code>{
     *   "login": "user",
     *   "password": "123",
     *   "id": "123",
     *   "text": "hello"
     * }</code></pre>
     *
     * <p><strong>Possible responses:</strong></p>
     *
     * <ul>
     *   <li>Invalid JSON format:
     *     <pre><code>{
     *       "success": false,
     *       "message": "Invalid request format",
     *       "exitCode": 3
     *     }</code></pre>
     *   </li>
     *
     *   <li>Missing required fields:
     *     <pre><code>{
     *       "success": false,
     *       "message": "Missing required fields",
     *       "exitCode": 3
     *     }</code></pre>
     *   </li>
     *
     *   <li>Whitelist mode enabled and user not in whitelist:
     *     <pre><code>{
     *       "success": false,
     *       "message": "Access denied: not in whitelist",
     *       "exitCode": 1
     *     }</code></pre>
     *   </li>
     *
     *   <li>Incorrect login or password:
     *     <pre><code>{
     *       "success": false,
     *       "message": "Incorrect login or password",
     *       "exitCode": 2
     *     }</code></pre>
     *   </li>
     *
     *   <li>User is banned:
     *     <pre><code>{
     *       "success": false,
     *       "message": "Account is banned",
     *       "exitCode": 4
     *     }</code></pre>
     *   </li>
     *
     *   <li>Success:
     *     <pre><code>{
     *       "success": true,
     *       "message": "Message sent successfully",
     *       "exitCode": 0
     *     }</code></pre>
     *   </li>
     * </ul>
     *
     * <p>Note: The user's verification status is determined by matching the provided {@code id} with the stored credentials.</p>
     */
    public void Run(Context ctx, ChatService chatService, boolean whitelistMode) {
        // Parse request body
        SendChatMessageRequest req;
        try {
            req = ctx.bodyAsClass(SendChatMessageRequest.class);
        } catch (Exception e) {
            ctx.json(new SendChatMessageResponse(false, "Invalid request format", 3));
            return;
        }
        
        // validation req json
        if (req.login == null || req.login.isBlank() ||
        req.password == null ||
        req.id == null || req.id.isBlank() ||
        req.text == null || req.text.isBlank()) {
            ctx.json(new SendChatMessageResponse(false, "Missing required fields", 3));
            return;
        }
        
        // Delegate business logic to service
        school.chat.services.dto.SendChatMessageRequest serviceReq = new school.chat.services.dto.SendChatMessageRequest(
            req.login, req.password, req.id, req.text
        );
        school.chat.services.dto.SendChatMessageResponse serviceResp = chatService.sendChatMessage(serviceReq, whitelistMode);
        
        // Success response
        ctx.json(new SendChatMessageResponse(serviceResp.success, serviceResp.message, serviceResp.exitCode));
    }
}
