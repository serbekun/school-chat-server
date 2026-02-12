package school.chat.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {

    /* ==================== */
    /* JSON representation  */
    /* ==================== */
    private static class ConfigData {
        public String serverFolder = "server_data/";
        public int port = 8080;
        public boolean addFreeWhitelist = true;
        public boolean whitelistMode = false;
    }

    private final ObjectMapper mapper;
    private ConfigData data;
    private final Path configPath;
    private final List<String> requiredDirectories;

    /* ==================== */
    /* Exposed path helpers */
    /* ==================== */
    public final FilePath filePath;

    /* --------------------- */
    /* Constructors          */
    /* --------------------- */
    public Config() {
        this("config.json");
    }

    public Config(String configFileName) {
        this.mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);

        this.configPath = Paths.get(configFileName).toAbsolutePath();
        this.data = new ConfigData();
        this.requiredDirectories = new ArrayList<>();
        this.filePath = new FilePath();

        load();
        updateRequiredFolders();
    }

    /* --------------------- */
    /* Load / Save           */
    /* --------------------- */
    private void load() {
        if (!Files.exists(configPath)) {
            System.out.println("[Config] Config file not found, creating default: " + configPath);
            save();
            return;
        }

        try {
            String json = Files.readString(configPath);
            ConfigData loaded = mapper.readValue(json, ConfigData.class);
            if (loaded != null) {
                data = loaded;
            }
            System.out.println("[Config] Loaded config from: " + configPath);
        } catch (Exception e) {
            System.err.println("[Config] Failed to load config: " + e.getMessage());
            e.printStackTrace();
            System.out.println("[Config] Using default configuration");
            save();
        }
    }

    public void save() {
        try {
            String json = mapper.writeValueAsString(data);
            Files.writeString(configPath, json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
            System.out.println("[Config] Saved to: " + configPath);
        } catch (IOException e) {
            System.err.println("[Config] Failed to save config: " + e.getMessage());
        }
    }

    /* --------------------- */
    /* Directory handling   */
    /* --------------------- */
    private void updateRequiredFolders() {
        requiredDirectories.clear();
        if (data.serverFolder != null && !data.serverFolder.isBlank()) {
            String path = data.serverFolder.endsWith("/") ? data.serverFolder : data.serverFolder + "/";
            data.serverFolder = path;
            requiredDirectories.add(path);
        }
    }

    public boolean createRequiredDirectories() {
        for (String dir : requiredDirectories) {
            try {
                Path p = Paths.get(dir);
                if (!Files.exists(p)) {
                    Files.createDirectories(p);
                    System.out.println("[Config] Created directory: " + dir);
                }
            } catch (IOException e) {
                System.err.println("[Config] Failed to create directory " + dir + " → " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    /* --------------------- */
    /* Updating values      */
    /* --------------------- */
    private void saveChangeInConfig() {
        updateRequiredFolders();
        save();
    }

    public void updatePort(int port) {
        if (port > 0 && port < 65536) {
            data.port = port;
            saveChangeInConfig();
        }
    }

    public void updateServerFolder(String serverFolder) {
        if (serverFolder != null && !serverFolder.isBlank()) {
            data.serverFolder = serverFolder.endsWith("/") ? serverFolder : serverFolder + "/";
            saveChangeInConfig();
        }
    }

    public void updateAddFreeWhitelist(boolean addFreeWhitelist) {
        data.addFreeWhitelist = addFreeWhitelist;
        saveChangeInConfig();
    }

    public void updateWhitelistMode(boolean whitelistMode) {
        data.whitelistMode = whitelistMode;
        saveChangeInConfig();
    }

    /* --------------------- */
    /* Getters              */
    /* --------------------- */
    public String getServerFolder()       { return data.serverFolder; }
    public int    getPort()               { return data.port; }
    public Path   getConfigPath()         { return configPath; }
    public boolean getAddFreeWhitelist()  { return data.addFreeWhitelist; }
    public boolean getWhitelistMode()     { return data.whitelistMode; }

    public List<String> getRequiredDirectories() {
        return Collections.unmodifiableList(requiredDirectories);
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(data);
        } catch (Exception e) {
            return "Config{ERROR: " + e.getMessage() + "}";
        }
    }

    /* ==================== */
    /* Path helper inner class */
    /* ==================== */
    public class FilePath {
        public String getChatMessagesSaveFilePath() {
            return data.serverFolder + "chat_messages.json";
        }
        public String getUsersSaveFilePath() {
            return data.serverFolder + "users.json";
        }
        public String getWhiteListSaveFilePath() {
            return data.serverFolder + "whitelist.json";
        }
        public String getVerificationTextFilePath() {
            return data.serverFolder + "verification_text.json";
        }
        public String getProhibitedLoginsFilePath() {
            return data.serverFolder + "prohibited_login.json";
        }
    }
}