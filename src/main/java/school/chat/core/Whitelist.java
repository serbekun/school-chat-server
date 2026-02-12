package school.chat.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Whitelist class save whitelisted ids in json file
 */
public class Whitelist {
    
    /**
     * whitelist save file path
     */
    private final String whiteListSaveFilePath;
    private final ObjectMapper mapper;

    public Whitelist(String whiteListSaveFilePath) {
        this.whiteListSaveFilePath = whiteListSaveFilePath;
        
        this.mapper = new ObjectMapper();
    }

    private static class WhitelistIds {
        public List<String> ids;
    }

    /**
     * Method for add id to server whitelist
     * 
     * @param id id what you want to add whitelist
     */
    public void addId(String id) {
        try {
            WhitelistIds data;

            Path path = Paths.get(whiteListSaveFilePath);

            // if file exist and not empty
            if (Files.exists(path) && Files.size(path) > 0) {
                String json = Files.readString(path);
                data = mapper.readValue(json, WhitelistIds.class);
            } else {
                data = new WhitelistIds();
            }

            // protection from null
            if (data.ids == null) {
                data.ids = new ArrayList<>();
            }

            // don't add duplicate
            if (!data.ids.contains(id)) {
                data.ids.add(id);
            }

            // write new json
            String newJson = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(data);

            Files.writeString(
                    path,
                    newJson,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to update whitelist", e);
        }
    }

    /**
     * Method for remove if from whitelist
     * 
     * @param id id what you want to remove
     */
    public void removeId(String id) {
        try {
            WhitelistIds data;

            Path path = Paths.get(whiteListSaveFilePath);

            // if file exist and not empty
            if (Files.exists(path) && Files.size(path) > 0) {
                String json = Files.readString(path);
                data = mapper.readValue(json, WhitelistIds.class);
            } else {
                return; // If the file is empty or does not exist, nothing to remove.
            }

            // protection from null
            if (data.ids == null || data.ids.isEmpty()) {
                return; // If there are no IDs in the whitelist, nothing to remove.
            }

            // Remove the id if it exists
            data.ids.remove(id);

            // write new json
            String newJson = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(data);

            Files.writeString(
                    path,
                    newJson,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to update whitelist", e);
        }
    }

    /**
     * 
     * @param id what you want to check contain in whitelist
     * @return true if id contain in whitelist. false if not contain in whitelist
     */
    public boolean ContainsId(String id) {
        try {
            WhitelistIds data;

            Path path = Paths.get(whiteListSaveFilePath);

            // if file exist and not empty
            if (Files.exists(path) && Files.size(path) > 0) {
                String json = Files.readString(path);
                data = mapper.readValue(json, WhitelistIds.class);
            } else {
                return false; // If the file is empty or does not exist, return false.
            }

            // protection from null
            if (data.ids == null || data.ids.isEmpty()) {
                return false; // If there are no IDs in the whitelist, return false.
            }

            return data.ids.contains(id);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read whitelist", e);
        }
    }

}