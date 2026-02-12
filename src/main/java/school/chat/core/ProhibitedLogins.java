package school.chat.core;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;

public class ProhibitedLogins {
    
    private static final String className = "[core/prohibited-logins]";

    /**
     * Prohibited logins in memory
     */
    private List<String> prohibitedLogin = new ArrayList<>();
    
    private final String prohibitedLoginSaveFilePath;
    private static final ObjectMapper mapper = new ObjectMapper();

    public ProhibitedLogins(String prohibitedLoginSaveFilePath) {
        this.prohibitedLoginSaveFilePath = prohibitedLoginSaveFilePath;

        LoadFromFile();
    }

    /**
     * 
     * Add prohibited login to server
     * 
     * @param login prohibited login to add
     * @return true if new prohibited login added, false if adding new prohibited login already exist
     */
    public boolean AddProhibitedLogin(String login) {
        if (prohibitedLogin.contains(login)) {
            return false;
        }
        prohibitedLogin.add(login);
        return true;
    }

    /**
     * 
     * Remove login by login
     * 
     * @param login prohibited login to remove
     * @return true if login removed false if login doesn't exist
     */
    public boolean RemoveProhibitedLoginByLogin(String login) {
        
        if (!prohibitedLogin.contains(login)) {
            return false;
        }

        int index = prohibitedLogin.indexOf(login);
        prohibitedLogin.remove(index);

        return true;
    }

    /**
     * 
     * Remove login by index
     * 
     * @param index login index
     * @return true if login removed, false if login didn't exist
     */
    public boolean RemoveProhibitedLoginByIndex(int index) {
        if (prohibitedLogin.size() < index) {
            return false;
        }

        prohibitedLogin.remove(index);
        return true;
    }

    /**
     * Save to file prohibited logins from memory
     */
    void SaveToFile() {

        try {
            mapper.writeValue(
                new File(prohibitedLoginSaveFilePath),
                prohibitedLogin
            );
        } catch (IOException e) {
            System.err.println("Error save prohibited logins to file " + e);
        }
            

    }

    /**
     * Load prohibited logins from file to memory
     */
    boolean LoadFromFile() {
        Path path = Paths.get(prohibitedLoginSaveFilePath);
        if (Files.exists(path)) {
            try {
                List<String> loaded = mapper.readValue(
                    new File(prohibitedLoginSaveFilePath),
                    new TypeReference<List<String>>() {}
                );
                this.prohibitedLogin = loaded != null ? loaded : new ArrayList<>();
                System.out.println(className + " Loaded " + this.prohibitedLogin.size() + " prohibited logins");
            } catch (IOException e) {
                System.out.println(className + " Error reading file: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                this.prohibitedLogin = new ArrayList<>();
            }
            return true;
        } else {
            try {
                Files.createFile(path);
                System.out.println(className + " Created new empty prohibited logins file");
                SaveToFile();
            } catch (IOException e) {
                System.err.println(className + " Error create file " + e);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check login is allowed or prohibited
     * 
     * @return true is login allowed. false login is prohibited
    */
    public boolean CheckLogin(String login) {
        if (prohibitedLogin == null) {
            System.err.println(className + " prohibitedLogin was null in CheckLogin - this should not happen");
            prohibitedLogin = new ArrayList<>();
        }
        return !prohibitedLogin.contains(login);
    }
}
