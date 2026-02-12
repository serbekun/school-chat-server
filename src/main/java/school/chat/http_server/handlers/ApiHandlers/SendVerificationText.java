package school.chat.http_server.handlers.ApiHandlers;

import io.javalin.http.Context;
import school.chat.http_server.handlers.ApiHandlers.dto.SendChatMessageRequest;
import school.chat.http_server.handlers.ApiHandlers.dto.SendVerificationTextResponse;
import school.chat.services.ChatService;

/**
 * <p>Handles the POST request to the <code>/v0/api/send_verification_text</code> endpoint.</p>
 *
 * <p><strong>Request body example:</strong></p>
 * <pre><code>{
 *   "login":    "user123",
 *   "password": "secret321",
 *   "id":       "abc123token",
 *   "text":     "I confirm this is my account"
 * }</code></pre>
 *
 * <p><strong>Possible responses:</strong></p>
 * <ul>
 *   <li>Invalid JSON format:
 *     <pre><code>{
 *       "success": false,
 *       "message": "Invalid json",
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
 *   <li>Whitelist mode enabled and ID not in whitelist:
 *     <pre><code>{
 *       "success": false,
 *       "message": "Access denied: not in whitelist",
 *       "exitCode": 1
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
 *   <li>Incorrect login or password:
 *     <pre><code>{
 *       "success": false,
 *       "message": "Incorrect login or password",
 *       "exitCode": 2
 *     }</code></pre>
 *   </li>
 *
 *   <li>Success:
 *     <pre><code>{
 *       "success": true,
 *       "message": null,
 *       "exitCode": 0
 *     }</code></pre>
 *   </li>
 * </ul>
 *
 * <p>The verification text is stored and can later be used to prove account ownership.</p>
 */
public class SendVerificationText {
    public void Run(Context ctx, ChatService chatService, boolean whitelistMode) {

    // get json body
    SendChatMessageRequest req;
    try {
        req = ctx.bodyAsClass(SendChatMessageRequest.class);
    } catch (Exception e) {
        ctx.json(new SendVerificationTextResponse(false, "Invalid json", 3));
        return;
    }

    // validate req json
    if (req.id == null || req.id.isBlank()
    || req.login == null || req.id.isBlank()
    || req.password == null || req.password.isBlank()
    || req.text == null || req.text.isBlank()) {
        ctx.json(new SendVerificationTextResponse(false, "Missing required fields", 3));
        return;
    }

    // Delegate business logic to service
    school.chat.services.dto.SendVerificationTextRequest serviceReq = new school.chat.services.dto.SendVerificationTextRequest(
        req.login, req.password, req.id, req.text
    );
    school.chat.services.dto.SendVerificationTextResponse serviceResp = chatService.sendVerificationText(serviceReq, whitelistMode);
    
    ctx.json(new SendVerificationTextResponse(serviceResp.success, serviceResp.message, serviceResp.exitCode));
    }
}