package model;

import exceptions.ObjectNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    // returns true, otherwise returns false
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

    // REQUIRES: there must be a saved password with given title
    // MODIFIES: this
    // EFFECTS: removes password log with given title
    public void deletePasswordLog(String title) {
        PasswordLog pl = null;
        for (PasswordLog p : passwordLogs) {
            if (p.getTitle().equals(title)) {
                pl = p;
            }
        }
        passwordLogs.remove(pl);
    }

    // REQUIRES: there must be a saved password with given title
    // EFFECTS: returns password log with given title
    public PasswordLog getPasswordLog(String title) {
        PasswordLog pl = null;
        for (PasswordLog p : passwordLogs) {
            if (p.getTitle().equals(title)) {
                pl = p;
            }
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

    // REQUIRES: order is one of: "alphabetical", "reverse"
    // EFFECTS: returns all titles of saved passwords sorted either alphabetically or reverse alphabetically, depending
    // on order
    public List<String> viewPasswordsSorted(String order) {
        List<String> plTitles = viewPasswords();

        if (order.equals("alphabetical")) {
            Collections.sort(plTitles);
        }
        if (order.equals("reverse")) {
            plTitles.sort(Collections.reverseOrder());
        }

        return plTitles;
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
    public JSONObject deleteLogFromJson(PasswordLog pl) {
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
    public JSONObject updateLogInJson(PasswordLog pl, String info, String value) {
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
