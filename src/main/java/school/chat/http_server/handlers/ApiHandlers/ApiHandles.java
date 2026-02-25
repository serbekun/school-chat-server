package school.chat.http_server.handlers.ApiHandlers;

import java.util.function.BooleanSupplier;
import io.javalin.Javalin;
import school.chat.services.UserService;
import school.chat.services.ChatService;
import school.chat.services.IdService;

public class ApiHandles {
    
    // getters from config
    private final BooleanSupplier getAddFreeWhitelist;
    private final BooleanSupplier getWhitelistMode;

    private final UserService userService;
    private final ChatService chatService;
    private final IdService idService;

    public ApiHandles(
        UserService userService,
        ChatService chatService,
        IdService idService,
        BooleanSupplier getAddFreeWhitelist,
        BooleanSupplier getWhitelistMode) {
            
        this.getAddFreeWhitelist = getAddFreeWhitelist;
        this.getWhitelistMode = getWhitelistMode;
        
        this.userService = userService;
        this.chatService = chatService;
        this.idService = idService;
    }

    public void InitApiHandles(Javalin svr) {
        // REST-style API routes
        svr.get("/v0/api/health", ctx -> new Health().Run(ctx));
        svr.post("/v0/api/ids", ctx -> new GetId().Run(ctx, idService, getAddFreeWhitelist.getAsBoolean()));
        svr.post("/v0/api/users", ctx -> new CreateUser().Run(ctx, userService, getWhitelistMode.getAsBoolean()));
        svr.post("/v0/api/messages", ctx -> new SendChatMessages().Run(ctx, chatService, getWhitelistMode.getAsBoolean()));
        svr.get("/v0/api/messages", ctx -> new GetChatMessages().Run(ctx, chatService, getWhitelistMode.getAsBoolean()));
        svr.post("/v0/api/verification-texts", ctx -> new SendVerificationText().Run(ctx, chatService, getWhitelistMode.getAsBoolean()));
    }

}
