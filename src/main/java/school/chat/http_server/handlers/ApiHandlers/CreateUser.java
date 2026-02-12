package school.chat.http_server.handlers.ApiHandlers;

import io.javalin.http.Context;
import school.chat.http_server.handlers.ApiHandlers.dto.CreateUserRequest;
import school.chat.http_server.handlers.ApiHandlers.dto.CreateUserResponse;
import school.chat.services.UserService;
import school.chat.services.dto.CreateUserServiceResponse;

/**
 * <p>Handles the POST request to the <code>/v0/api/create_user</code> endpoint.</p>
 *
 * <p>Creates a new user account in the system.</p>
 *
 * <p><strong>Request body example:</strong></p>
 * <pre><code>{
 *   "id":       "xyz987token",
 *   "login":    "new-user",
 *   "password": "qwerty123"
 * }</code></pre>
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
 *   <li>Login is prohibited (blacklisted / reserved):
 *     <pre><code>{
 *       "success": false,
 *       "message": "This login is prohibited",
 *       "exitCode": 5    // actual code depends on ProhibitedLogins implementation
 *     }</code></pre>
 *   </li>
 *
 *   <li>Error during creation (e.g. login already taken):
 *     <pre><code>{
 *       "success": false,
 *       "message": "Error creating User",
 *       "exitCode": 3
 *     }</code></pre>
 *   </li>
 *
 *   <li>Success:
 *     <pre><code>{
 *       "success": true,
 *       "message": "User created successfully",
 *       "exitCode": 0
 *     }</code></pre>
 *   </li>
 * </ul>
 */
public class CreateUser {
    
    public void Run(Context ctx, UserService userService, boolean whitelistMode) {

        CreateUserRequest req = ctx.bodyAsClass(CreateUserRequest.class);

        CreateUserServiceResponse cUSR = userService.CreateUser(req.login, req.password, req.id, whitelistMode);

        ctx.json(new CreateUserResponse(cUSR.success, cUSR.message, cUSR.exitCode));

    }
}
