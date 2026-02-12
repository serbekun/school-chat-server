
package school.chat.http_server;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import io.javalin.Javalin;
import school.chat.http_server.handlers.ResourcesHandlers;
import school.chat.http_server.handlers.ApiHandlers.ApiHandles;

public class Server {

    private final Javalin svr;
    private final ApiHandles handles;
    private final ResourcesHandlers resourcesHandlers;

    int port;

    private boolean serverIsStarted = false;

    public Server(Javalin svr, ApiHandles handles, int port) {
        this.svr = svr;
        this.handles = handles;
        this.resourcesHandlers = new ResourcesHandlers();
        this.port = port;
        initServerHandles();
        Set404Error();

        start();
    }
    
    /**
     * init API and resources endpoints
     */
    private void initServerHandles() {
        handles.InitApiHandles(svr);

        svr.get("/", resourcesHandlers::index);
        svr.get("/v0/page/{page}", resourcesHandlers::page);
        svr.get("/v0/images/google_icon.png", resourcesHandlers::google_icon);
    }
    
    /**
     * Set custom 404 page for server
     */
    private void Set404Error() {
        svr.error(404, ctx -> {
            ctx.contentType("text/html; charset=utf-8");

            try (InputStream is = getClass().getResourceAsStream("/html/404.html")) {
                if (is == null) {
                    ctx.result("404 Not Found");
                    return;
                }
                ctx.result(new String(is.readAllBytes(), StandardCharsets.UTF_8));
            }
        });
    }

    /**
     * 
     * Start server
     * 
     * @return 0 server start successfully, -1 server didn't start, 1 server already started
     */
    public int start() {
        if (serverIsStarted) {
            return 1;
        }
        try {
            svr.start();
            System.out.println("server started successfully");
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        serverIsStarted = true;
        return 0;
    }

    /**
     * 
     * Stop server
     * 
     * @return true if server stopped , false if server already stopped
     */
    public boolean stop() {
        if (!serverIsStarted) {
            return false;
        }
        svr.stop();
        serverIsStarted = false;
        return true;
    }

    public int port() {
        return port;
    }

    public boolean serverIsStarted() {
        return serverIsStarted;
    }

}