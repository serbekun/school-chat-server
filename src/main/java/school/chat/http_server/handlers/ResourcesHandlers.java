package school.chat.http_server.handlers;

import io.javalin.http.Context;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResourcesHandlers {
private static final Map<String, String> HTML_CACHE = new HashMap<>();

    private static final byte[] GOOGLE_ICON = loadBinary("images/google_icon.png");

    /**
     * 
     * load binary from file
     * 
     * @param filename filename that load binary
     * @return
     */
    private static byte[] loadBinary(String filename) {
        try (InputStream is =
                ResourcesHandlers.class
                    .getClassLoader()
                    .getResourceAsStream(filename)) {

            if (is == null) {
                throw new RuntimeException(filename + " NOT FOUND");
            }

            return is.readAllBytes();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + filename, e);
        }
    }

    /** resources in jar file */
    static {
        load("index");
        load("chat");
        load("create_user");
        load("verification_text");
    }

    /**
     * load files to jar
     */
    private static void load(String name) {
        try (InputStream is = ResourcesHandlers.class
                .getClassLoader()
                .getResourceAsStream("html/" + name + ".html")) {

            if (is != null) {
                HTML_CACHE.put(name,
                    new String(is.readAllBytes(), StandardCharsets.UTF_8));
            }
        } catch (Exception ignored) {}
    }

    /**
     * return index html
     */
    public void index(Context ctx) {
        String html = HTML_CACHE.get("index");

        if (html == null) {
            ctx.status(404);
            return;
        }

        ctx.contentType("text/html; charset=utf-8");
        ctx.result(html);   
    }

    /**
     * return site pages
     */
    public void page(Context ctx) {
        String page = ctx.pathParam("page");
        String html = HTML_CACHE.get(page);

        if (html == null) {
            ctx.status(404);
            return;
        }

        ctx.contentType("text/html; charset=utf-8");
        ctx.result(html);
    }

    /**
     * 
     * Return google icon
     * 
     */
    public void google_icon(Context ctx) {
        ctx.contentType("image/png");
        ctx.result(GOOGLE_ICON);
    }
}
