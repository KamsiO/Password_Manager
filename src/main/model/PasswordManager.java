package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A representation of a collection of stored passwords. New passwords can be stored, and previously stored passwords
 * can be deleted. Stored passwords can be arranged by most recently added, alphabetical order, or reverse
 * alphabetical order.
 */

public class PasswordManager {
    private List<PasswordLog> passwordLogs;

    // EFFECTS: makes an empty password manager
    public PasswordManager() {
        passwordLogs = new ArrayList<>();
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
        List<String> plTitles = new ArrayList<>();
        for (PasswordLog pl : passwordLogs) {
            plTitles.add(pl.getTitle());
        }

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
}
