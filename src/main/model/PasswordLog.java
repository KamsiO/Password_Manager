package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * A representation of a stored password. A password is stored under a title, and has an associated username and url
 * and a notes section. All of these stored attributes including the password and title can be updated.
 */

public class PasswordLog implements Writable {
    private String title;
    private Password password;
    private String url;
    private String username;
    private String notes;

    // EFFECTS: makes a password log with the given title and password, and empty related info
    public PasswordLog(Password pw, String title) {
        this.title = title;
        password = pw;
        url = "";
        username = "";
        notes = "";
    }

    // EFFECTS: makes a password log with the given title, password, and related info
    public PasswordLog(Password pw, String title, String user, String url, String notes) {
        this.title = title;
        password = pw;
        this.url = url;
        username = user;
        this.notes = notes;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("password", password.getPassword());
        json.put("title", title);
        json.put("username", username);
        json.put("url", url);
        json.put("notes", notes);
        return json;
    }

    // MODIFIES: this
    // EFFECTS: changes the notes to n
    public void setNotes(String n) {
        notes = n;
    }

    // EFFECTS: returns the notes in the password log
    public String getNotes() {
        return notes;
    }

    // MODIFIES: this
    // EFFECTS: changes the associated url to url
    public void setURL(String url) {
        this.url = url;
    }

    // EFFECTS: returns the associated url of password log
    public String getURL() {
        return url;
    }

    // MODIFIES: this
    // EFFECTS: changes the associated username to u
    public void setUsername(String u) {
        username = u;
    }

    // EFFECTS: returns password of password log
    public String getUsername() {
        return username;
    }

    // MODIFIES: this
    // EFFECTS: changes the password to pw
    public void updatePassword(Password pw) {
        password = pw;
    }

    // EFFECTS: returns password of password log
    public Password getPassword() {
        return password;
    }

    // MODIFIES: this
    // EFFECTS: changes title of password log to t
    public void updateTitle(String t) {
        title = t;
    }

    // EFFECTS: returns title of password log
    public String getTitle() {
        return title;
    }
}
