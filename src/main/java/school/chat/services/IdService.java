package school.chat.services;

import school.chat.core.Tokens;
import school.chat.core.Whitelist;
import school.chat.services.dto.IdResponse;

/**
 * Service for ID/token generation and management.
 */
public class IdService {

    private final Tokens tokens;
    private final Whitelist whitelist;

    public IdService(Tokens tokens, Whitelist whitelist) {
        this.tokens = tokens;
        this.whitelist = whitelist;
    }

    /**
     * Generate a new ID and optionally add it to whitelist.
     */
    public IdResponse generateId(boolean addToWhitelist) {
        String id = tokens.genToken();

        if (addToWhitelist) {
            whitelist.addId(id);
        }

        return new IdResponse(id);
    }
}
