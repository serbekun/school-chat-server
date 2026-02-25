package school.chat.http_server.handlers.ApiHandlers;

import io.javalin.http.Context;
import school.chat.http_server.handlers.ApiHandlers.dto.IdResponse;
import school.chat.services.IdService;

/**
 * <p>Handles the request to generate a new token/ID (POST <code>/v0/api/ids</code>).</p>
 *
 * <p>Generates a new unique token and optionally adds it to the whitelist immediately.</p>
 *
 * <p><strong>Response:</strong></p>
 * <pre><code>{
 *   "id": "a1b2c3d4e5f6g7h8i9j0"
 * }</code></pre>
 *
 * <p>If <code>AddFreeWhitelist = true</code>, the generated ID is automatically added to the whitelist.</p>
 */
public class GetId {
    public void Run(Context ctx, IdService idService, boolean addFreeWhitelist) 
    {
        school.chat.services.dto.IdResponse serviceResp = idService.generateId(addFreeWhitelist);
        ctx.json(new IdResponse(serviceResp.id));
    }

}
