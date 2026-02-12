package school.chat.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class VerificationText {
    
    private final String verificationTextJsonFilePath;
    private final List<VerificationTextJson> verificationTexts;
    private final ObjectMapper mapper;
    
    public VerificationText(String verificationTextJsonFilePath) {
        this.verificationTextJsonFilePath = verificationTextJsonFilePath;
        this.verificationTexts = new ArrayList<>();
        this.mapper = new ObjectMapper();
        // Pretty print JSON for readability
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // json that contain verification text
    class VerificationTextJson {
        public String id;
        public String login;
        public String text;
        public String password;

        public VerificationTextJson() {}

        public VerificationTextJson(String id, String login, String text) {
            this.id = id;
            this.login = login;
            this.text = text;
        }
    }

    /**
     * Appends new verification text
     */
    public void AppendVerificationText(String id, String login, String text) {
        VerificationTextJson verificationTextJson = new VerificationTextJson(id, login, text);
        verificationTexts.add(verificationTextJson);
        SaveToFile();
    }

    /**
     * Removes a verificationTexts by its index (0-based). Throws exception if index is invalid.
     */
    public void RemoveMessageByNumber(int number) {
        if (number < 0 || number >= verificationTexts.size()) {
            System.err.println("Out of bounds messages index");
            return;
        }
        verificationTexts.remove(number);
        SaveToFile();
    }

    /**
     * Loads messages from the JSON file into memory
     */
    public void LoadFromFile() {
        File file = new File(verificationTextJsonFilePath);
        if (!file.exists() || file.length() == 0) {
            verificationTexts.clear();
            return;
        }

        try {
            List<VerificationTextJson> loaded = mapper.readValue(file, new TypeReference<List<VerificationTextJson>>() {});
            verificationTexts.clear();
            verificationTexts.addAll(loaded);
        } catch (IOException e) {
            System.err.println("FAILED TO LOAD FILE: " + verificationTextJsonFilePath);
            e.printStackTrace();
            verificationTexts.clear();
        }
    }

    /**
     * Saves current in-memory messages to the JSON file
     */
    public void SaveToFile() {
        try {
            // Ensure parent directories exist
            File file = new File(verificationTextJsonFilePath);
            if (file.getParentFile() != null) {
                Files.createDirectories(file.getParentFile().toPath());
            }

            mapper.writeValue(file, verificationTexts);
        } catch (IOException e) {
            System.err.println("Failed to save chat messages to file: " + e.getMessage());
        }
    }

    /**
     * delete all verifications text
     */
    public void Clear() {
        verificationTexts.clear();
        SaveToFile();
    }

    /**
     * Returns an unmodifiable view of the current chat messages
     */
    public List<VerificationTextJson> GetVerificationTexts() {
        return List.copyOf(verificationTexts); // immutable copy for safety
    }
}
