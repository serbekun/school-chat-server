package school.chat.http_server.handlers.ApiHandlers;

import io.javalin.http.Context;

import school.chat.http_server.handlers.ApiHandlers.dto.HealthResponse;

/**
 * <p>Handles the GET request to the <code>/health</code> or <code>/v0/api/health</code> endpoint.</p>
 *
 * <p>Simple server health check endpoint.</p>
 *
 * <p><strong>Always returns:</strong></p>
 * <pre><code>{
 *   "healthy": true
 * }</code></pre>
 *
 * <p>If the server responds, it is considered alive and operational.</p>
 */
public class Health {
    public void Run(Context ctx) {
        ctx.json(new HealthResponse(true));
    }
}
