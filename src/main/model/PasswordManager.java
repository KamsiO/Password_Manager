package model;

import exceptions.ObjectNotFoundException;
import exceptions.PasswordLogDoesNotExistException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.SortOrder.ALPHABETICAL;

/**
 * A representation of a collection of stored passwords. New passwords can be stored, and previously stored passwords
 * can be deleted. Stored passwords can be arranged by most recently added, alphabetical order, or reverse
 * alphabetical order.
 */

public class PasswordManager implements Writable {
    private List<PasswordLog> passwordLogs;
    private JSONObject json;

    // EFFECTS: makes an empty password manager
    public PasswordManager() {
        passwordLogs = new ArrayList<>();
        json = null;
    }

    // MODIFIES: this
    // EFFECTS: if a password under the given case-sensitive title doesn't exist, saves password under title and
    //          returns true, otherwise returns false
    public boolean addPasswordLog(Password pw, String title) {
        for (PasswordLog pl : passwordLogs) {
            if (pl.getTitle().equals(title)) {
                return false;
            }
        }
        PasswordLog pl = new PasswordLog(pw, title);
        passwordLogs.add(0, pl);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: if a password log with the given case-sensitive title doesn't exist, adds password log to password
    //              manager and returns true, otherwise returns false
    public boolean addPasswordLog(PasswordLog log, String title) {
        for (PasswordLog pl : passwordLogs) {
            if (pl.getTitle().equals(title)) {
                return false;
            }
        }
        passwordLogs.add(0, log);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: if a password log with the given title does not exist, throws PasswordLogDoesNotExistException
    //          otherwise, removes password log with given title
    public void deletePasswordLog(String title) throws PasswordLogDoesNotExistException {
        PasswordLog pl = null;
        for (PasswordLog p : passwordLogs) {
            if (p.getTitle().equals(title)) {
                pl = p;
            }
        }
        if (pl == null) {
            throw new PasswordLogDoesNotExistException();
        }
        passwordLogs.remove(pl);
    }

    // EFFECTS: if a password log with the given title does not exist, throws PasswordLogDoesNotExistException
    //          returns password log with given title
    public PasswordLog getPasswordLog(String title) throws PasswordLogDoesNotExistException {
        PasswordLog pl = null;
        for (PasswordLog p : passwordLogs) {
            if (p.getTitle().equals(title)) {
                pl = p;
            }
        }
        if (pl == null) {
            throw new PasswordLogDoesNotExistException();
        }
        return pl;
    }

    // EFFECTS: returns the titles of all saved passwords
    public List<String> viewPasswords() {
        List<String> plTitles = new ArrayList<>();
        for (PasswordLog pl : passwordLogs) {
            plTitles.add(pl.getTitle());
        }
        return plTitles;
    }

    // EFFECTS: returns all titles of saved passwords sorted either alphabetically or reverse alphabetically, depending
    // on order
    public List<String> viewPasswordsSorted(SortOrder order) {
        List<String> plTitles = viewPasswords();

        if (order == ALPHABETICAL) {
            Collections.sort(plTitles);
        } else {
            plTitles.sort(Collections.reverseOrder());
        }

        return plTitles;
    }

    // EFFECTS: returns list of password logs with titles that contain given string query
    public List<PasswordLog> searchPasswords(String query) {
        //https://www.geeksforgeeks.org/trim-remove-leading-trailing-spaces-string-java/
        Pattern pattern = Pattern.compile(query.toLowerCase().trim());
        Matcher matcher;

        List<PasswordLog> logs = new ArrayList<>();

        for (PasswordLog log : passwordLogs) {
            matcher = pattern.matcher(log.getTitle().toLowerCase());
            if (matcher.find()) {
                logs.add(log);
            }
        }
        return logs;
    }

    // EFFECTS: returns password logs in password manager
    public List<PasswordLog> getPasswordLogs() {
        return passwordLogs;
    }


    // MODIFIES: this
    // EFFECTS: adds a json object that represents the given password log
    public JSONObject addLogToJson(PasswordLog pl) {
        if (json == null) {
            json = toJson();
        } else {
            JSONArray logs = json.getJSONArray("passwordLogs");
            logs.put(pl.toJson());
        }

        return json;
    }

    // MODIFIES: this
    // EFFECTS: if the object to delete is not found, throws ObjectNotFoundException
    //          otherwise deletes the json object that represents the given password log
    public JSONObject deleteLogFromJson(PasswordLog pl) throws ObjectNotFoundException {
        if (json == null) {
            json = toJson();
        }
        int deleteIndex = 0;
        boolean foundObject = false;
        JSONArray logs = json.getJSONArray("passwordLogs");
        for (int i = 0; i < logs.length(); i++) {
            JSONObject log = logs.getJSONObject(i);
            if (log.getString("title").equals(pl.getTitle())) {
                deleteIndex = i;
                foundObject = true;
                break;
            }
        }
        if (!foundObject) {
            throw new ObjectNotFoundException();
        }
        logs.remove(deleteIndex);
        return json;
    }

    // MODIFIES: this
    // EFFECTS: if the object to update is not found, throws ObjectNotFoundException
    //          otherwise, updates the key value in the json file for the json object that represents given password log
    public JSONObject updateLogInJson(PasswordLog pl, String info, String value) throws ObjectNotFoundException {
        if (json == null) {
            json = toJson();
        }
        JSONArray logs = json.getJSONArray("passwordLogs");
        JSONObject toUpdate = null;
        for (int i = 0; i < logs.length(); i++) {
            JSONObject log = logs.getJSONObject(i);
            if (log.getString("title").equals(pl.getTitle())) {
                toUpdate = log;
                break;
            }
        }
        if (toUpdate == null) {
            throw new ObjectNotFoundException();
        }
        toUpdate.remove(info);
        if (info == "password") {
            value = pl.encrypt();
        }
        toUpdate.put(info, value);

        return json;
    }

    @Override
    public JSONObject toJson() { //credit: JsonSerializationDemo
        JSONObject json = new JSONObject();
        json.put("passwordLogs", logsToJson());
        return json;
    }

    // EFFECTS: returns the password logs in this password manager as a JSON array
    private JSONArray logsToJson() {
        JSONArray jsonArray = new JSONArray();
        PasswordLog log;

        for (int i = passwordLogs.size() - 1; i >= 0; i--) {
            log = passwordLogs.get(i);
            jsonArray.put(log.toJson());
        }

        return jsonArray;
    }
}
