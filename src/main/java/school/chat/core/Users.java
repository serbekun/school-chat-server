package school.chat.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.HashMap;

/**
 * Class Users manage chat users and save to file
 */
public class Users {
    /**
     * Users data json database file path
     */ 
    private final String usersSaveFilePath;

    /**
     * Jackson ObjectMapper object;
     */
    private final ObjectMapper mapper;

    /**
     * Data about users for cash on memory for speed
     */
    private Map<String, User> usersByLogin; // Key: login, Value: User

    public Users(String usersSaveFilePath) {
        this.usersSaveFilePath = usersSaveFilePath;
        this.mapper = new ObjectMapper();
        this.usersByLogin = new HashMap<>();
        loadFromDisk(false);
    }

    // Inner User class with JSON field name mappings
    public static class User {
        public String id;

        @JsonProperty("password_hash")
        public String password;

        public boolean verified;
        public int degreeOfAccess;
        public boolean banned;

        public User() {}

        public User(String login,
            String password,
            boolean verified,
            int degreeOfAccess,
            String id,
            boolean banned) {
            
            this.id = id;
            this.password = password;
            this.verified = verified;
            this.degreeOfAccess = degreeOfAccess;
            this.banned = banned;
        }
    }

    /**
     * Create new user
     * 
     * @param login user login 
     * @param password user password
     * @param verified user is verified
     * @param degreeOfAccess user degree of access
     * @param id user id
     * @return true user create successfully and saved at file,
     *  false if duplicated login or error save to disk
     */
    public synchronized boolean addUser(String login, String password, boolean verified,
                                        int degreeOfAccess, String id) {
        if (usersByLogin.containsKey(login)) {
            return false; // Duplicate login
        }

        User newUser = new User(login, password, verified, degreeOfAccess, id, false);
        usersByLogin.put(login, newUser);
        return saveToDisk();
    }

    /**
     * Remove user by login
     */
    public synchronized boolean RemoveUser(String login) {
        if (usersByLogin.remove(login) != null) {
            return saveToDisk();
        }
        return false;
    }

    /**
     * 
     * delete all users
     * 
     * @return true if save to disk successfully, false if error save to disk
     */
    public synchronized boolean clearUsers() {
        usersByLogin.clear();
        return saveToDisk();
    }

    /**
     * Checks if a user with the given login is banned.
     */
    public synchronized boolean isUserBanned(String login) {
        User user = usersByLogin.get(login);
        return user != null && user.banned;
    }

    public synchronized boolean isUserVerified(String login) {
        User user = usersByLogin.get(login);
        return user.verified;
    }

    /**
     * Check if login and password match
     */
    public synchronized boolean checkUser(String login, String password) {
        if (isUserBanned(login)) {
            return false;
        }
        User user = usersByLogin.get(login);
        return user != null && password.equals(user.password);
    }

    /**
     * Checks if a user exists with the given login, password, and ID, and is verified.
     */
    public synchronized boolean checkUserVerified(String login, String password, String id) {
        if (isUserBanned(login)) {
            return false;
        }
        User user = usersByLogin.get(login);
        return user != null &&
               password.equals(user.password) &&
               id.equals(user.id) &&
               user.verified;
    }

    /**
     * Returns the degree of access for a user.
     */
    public synchronized int getUserDegreeOfAccess(String login) {
        if (isUserBanned(login)) {
            return -1;
        }
        User user = usersByLogin.get(login);
        return user.degreeOfAccess;

    }

    /**
     * Get user by login
     */
    public synchronized User getUser(String login) {
        return usersByLogin.get(login);
    }

    /**
     * Load users from disk (now as Map<String, User>)
     */
    private synchronized void loadFromDisk(boolean is_server_start) {
        Path path = Paths.get(usersSaveFilePath);
        try {
            if (Files.exists(path) && Files.size(path) > 0) {
                String json = Files.readString(path);
                // Deserialize as Map<String, User>
                Map<String, User> loaded = mapper.readValue(json, new TypeReference<Map<String, User>>() {});
                if (loaded != null) {
                    this.usersByLogin = loaded;
                } else {
                    this.usersByLogin = new HashMap<>();
                }
            }
        } catch (IOException e) {
            if (!is_server_start) {
                System.err.println("Failed to load users from " + usersSaveFilePath + ", starting with empty map");
            }
            this.usersByLogin = new HashMap<>();
        }
    }

    /**
     * Save users to disk as JSON object { "login": user, ... }
     */
    private synchronized boolean saveToDisk() {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usersByLogin);
            Files.writeString(Paths.get(usersSaveFilePath), json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save users to disk: " + e.getMessage());
            return false;
        }
    }

    /**
     * 
     * Return users info Read only
     * 
     * @return users Map
     */
    public synchronized Map<String, User> getAllUsers() {
        return Map.copyOf(usersByLogin);
    }

    /**
     * ban user by login
     */
    public synchronized void BanUser(String login) {
        User user = usersByLogin.get(login);
        if (user == null) return;

        user.banned = true;
        saveToDisk();
    }

    /**
     * un ban user by login
     */
    public synchronized void unBanUser(String login) {
        User user = usersByLogin.get(login);
        if (user == null) return;

        user.banned = false;
        saveToDisk();
    }

    /**
     * verify user by login
     */
    public synchronized void VerifyUser(String login) {
        User user = usersByLogin.get(login);
        if (user == null) return;

        user.verified = true;
        saveToDisk();
    }

    /**
     * un verify user by login
     */
    public synchronized void UnVerifyUser(String login) {
        User user = usersByLogin.get(login);
        if (user == null) return;

        user.verified = false;
        saveToDisk();
    }

}