package school.chat.services;

import school.chat.core.Whitelist;

public class WhitelistService {

    private final Whitelist whitelist;

    public WhitelistService(Whitelist whitelist) {
        this.whitelist = whitelist;
    }

    /**
     * 
     * Check id is contain in whitelist
     * 
     * @param id what you want to check contain in whitelist
     * @return true if id contain in whitelist. false if not contain in whitelist
     */
    public boolean ContainsId(String id) {
        return whitelist.ContainsId(id);
    }

    /**
     * 
     * Add id to whitelist
     * 
     * @param id id to add
     */
    public void AddId(String id) {
        whitelist.addId(id);
    }

    /**
     * 
     * Remove if from whitelist
     * 
     * @param id id to remove
     */
    public void RemoveId(String id) {
        whitelist.removeId(id);
    }
}
