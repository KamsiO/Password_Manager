package persistence;

import model.Password;
import model.PasswordLog;
import model.PasswordManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that parses password manager from JSON data stored in file

public class JsonReader { //largely influenced by JsonSerializationDemo
                            // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads password manager from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PasswordManager read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePasswordManager(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses password manager from JSON object and returns it
    private PasswordManager parsePasswordManager(JSONObject jsonObject) {
        PasswordManager pm = new PasswordManager();
        addLogs(pm, jsonObject);
        return pm;
    }

    // MODIFIES: PasswordManager
    // EFFECTS: parses password logs from JSON object and adds them to password manager
    private void addLogs(PasswordManager pm, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("passwordLogs");
        for (Object json : jsonArray) {
            JSONObject nextLog = (JSONObject) json;
            addLog(pm, nextLog);
        }
    }

    // MODIFIES: PasswordManager
    // EFFECTS: parses password log from JSON object and adds it to password manager
    private void addLog(PasswordManager pm, JSONObject jsonObject) {
        Password pw = new Password(jsonObject.getString("password"));
        String title = jsonObject.getString("title");
        String url = jsonObject.getString("url");
        String user = jsonObject.getString("username");
        String notes = jsonObject.getString("notes");
        PasswordLog pl = new PasswordLog(pw, title, user, url, notes);
        pm.addPasswordLog(pl, title);
    }
}
