package school.chat.services;

import school.chat.core.ProhibitedLogins;

public class ProhibitedLoginService {
    
    private ProhibitedLogins prohibitedLogins;

    public ProhibitedLoginService(ProhibitedLogins prohibitedLogins) {
        this.prohibitedLogins = prohibitedLogins;
    }

    /**
     * Check login is allowed or prohibited
     * 
     * @return true is login allowed. false login is prohibited
    */
    public boolean CheckLogin(String login) {
        return prohibitedLogins.CheckLogin(login);
    }

    /**
     * 
     * Remove login by index
     * 
     * @param index login index
     * @return true if login removed, false if login didn't exist
     */
    public boolean RemoveProhibitedLoginByIndex(int index) {
        return prohibitedLogins.RemoveProhibitedLoginByIndex(index);
    }

    /**
     * 
     * Remove login by login
     * 
     * @param login prohibited login to remove
     * @return true if login removed false if login doesn't exist
     */
    public boolean RemoveProhibitedLoginByLogin(String login) {
        return prohibitedLogins.RemoveProhibitedLoginByLogin(login);
    }

    /**
     * 
     * Add prohibited login to server
     * 
     * @param login prohibited login to add
     * @return true if new prohibited login added, false if adding new prohibited login already exist
     */
    public boolean AddProhibitedLogin(String login) {
        return prohibitedLogins.AddProhibitedLogin(login);
    }
}
