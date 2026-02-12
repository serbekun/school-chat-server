package school.chat;

import java.io.File;
import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;

import school.chat.CommandLine.CommandLine;
// import core
import school.chat.core.Chat;
import school.chat.core.Config;
import school.chat.core.ProhibitedLogins;
import school.chat.core.Tokens;
import school.chat.core.Users;
import school.chat.core.VerificationText;
import school.chat.core.Whitelist;

// import http server
import school.chat.http_server.Server;
import school.chat.http_server.handlers.ApiHandlers.ApiHandles;

// import core services
import school.chat.services.UserService;
import school.chat.services.ChatService;
import school.chat.services.IdService;
import school.chat.services.ProhibitedLoginService;
import school.chat.services.WhitelistService;

public class Main {

    /**
     * Create need for server folders
     */
    private static void MakeNeededFolder() {
        File folder = new File("server_data");

        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdir();
        }
    }

    public static void main(String[] args) {

        MakeNeededFolder();
        
        try {
            // init objects
            Config svr_config = new Config();

            Javalin svr = Javalin.create(config -> {
                config.registerPlugin(new SslPlugin(ssl -> {
                    // Load PEM certificates
                    try {
                        ssl.pemFromPath("keys/fullchain.pem", "keys/privkey.pem");

                    } catch (Exception e) {
                        System.err.println("[main] Error could not find keys files in 'keys/fullchain.pem & 'keys/privkey.pem'");
                        System.err.println("# create a new private key and self-signed certificate (valid 1 year)");
                        System.err.println("openssl req -x509 -newkey rsa:4096 -nodes \\");
                        System.err.println("  -keyout server.key -out server.crt -days 365 \\");
                        System.err.println("  -subj \"/CN=localhost\"");
                        System.err.println();
                        System.err.println("# create a PKCS#12 keystore from key+cert (password: changeit)");
                        System.err.println("openssl pkcs12 -export -out keys/keystore.p12 \\");
                        System.err.println("  -inkey server.key -in server.crt -name schoolchat -passout pass:changeit");
                        System.err.println();
                        System.err.println("# secure the key files (optional)");
                        System.err.println("chmod 600 keys/keystore.p12");
                    }
                    
                    // Configure HTTPS only
                    ssl.secure = true;
                    ssl.securePort = svr_config.getPort();
                    ssl.insecure = false;
                    
                    // HTTP/2 support
                    ssl.http2 = true;
                    ssl.sniHostCheck = false;
                }));
            });

            // init core
            Chat chat = new Chat(svr_config.filePath.getChatMessagesSaveFilePath());
            Users users = new Users(svr_config.filePath.getUsersSaveFilePath());
            Whitelist whitelist = new Whitelist(svr_config.filePath.getWhiteListSaveFilePath());
            ProhibitedLogins prohibitedLogins = new ProhibitedLogins(svr_config.filePath.getProhibitedLoginsFilePath());
            Tokens tokens = new Tokens("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890", 64);
            VerificationText verificationText = new VerificationText(svr_config.filePath.getVerificationTextFilePath());
            
            // Initialize services
            WhitelistService whitelistService = new WhitelistService(whitelist);
            ProhibitedLoginService prohibitedLoginService = new ProhibitedLoginService(prohibitedLogins);
            UserService userService = new UserService(users, whitelistService, prohibitedLoginService);
            ChatService chatService = new ChatService(chat, users, whitelist, verificationText, prohibitedLogins);
            IdService idService = new IdService(tokens, whitelist);

            // init api handles and give needed services
            ApiHandles handles = new ApiHandles(
                userService,
                chatService,
                idService,
                svr_config::getAddFreeWhitelist,
                svr_config::getWhitelistMode
            );
            school.chat.http_server.Server server = new Server(svr, handles, svr_config.getPort());

            Thread httpServerThread = new Thread(() -> {
                server.start();
            }, "http-server-thread");

            Thread CommandLineThread = new Thread(() -> {
                school.chat.CommandLine.CommandLine commandLine = new CommandLine(svr ,chatService, userService);
                commandLine.run();
            }, "command-line-thread");

            httpServerThread.start();
            CommandLineThread.start();
        } catch (Exception e) {
            System.err.println("Fatal error during startup: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}