package persistence;

import model.PasswordLog;
import model.PasswordManager;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter { //largely based off of JsonSerializationDemo
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    //              be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // REQUIRES: function is one of "add", "delete", "update"
    // MODIFIES: this
    // EFFECTS: writes JSON representation of password manager to file
    public void write(PasswordManager pm, String function, PasswordLog pl, String info, String value) {
        JSONObject json;
        switch (function) {
            case "add":
                json = pm.addLogToJson(pl);
                break;
            case "delete":
                json = pm.deleteLogFromJson(pl);
                break;
            default:
                json = pm.updateLogInJson(pl, info, value);
                break;
        }
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}

