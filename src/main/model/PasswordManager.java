package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A representation of a collection of stored passwords. New passwords can be stored, and previously stored passwords
 * can be deleted. Stored passwords can be arranged by most recently added, alphabetical order, or reverse
 * alphabetical order.
 */

public class PasswordManager {
    private LinkedList<PasswordLog> pm;

    // EFFECTS: makes an empty password manager
    public PasswordManager() {
        pm = new LinkedList<>();
    }

    // MODIFIES: this
    // EFFECTS: if a password under the given case-sensitive title doesn't exist, saves password under title and
                    // returns true, otherwise returns false
    public boolean addPasswordLog(Password pw, String title) {
        for (PasswordLog pl : pm) {
            if (pl.getTitle().equals(title)) {
                return false;
            }
        }
        PasswordLog pl = new PasswordLog(pw, title);
        pm.addFirst(pl);
        return true;
    }

    // REQUIRES: there must be a saved password with given title
    // MODIFIES: this
    // EFFECTS: removes password log with given title
    public void deletePasswordLog(String title) {
        PasswordLog pl = null;
        for (PasswordLog p : pm) {
            if (p.getTitle().equals(title)) {
                pl = p;
            }
        }
        pm.remove(pl);
    }

    // REQUIRES: there must be a saved password with given title
    // EFFECTS: returns password log with given title
    public PasswordLog getPasswordLog(String title) {
        PasswordLog pl = null;
        for (PasswordLog p : pm) {
            if (p.getTitle().equals(title)) {
                pl = p;
            }
        }
        return pl;
    }

    // EFFECTS: returns the titles of all saved passwords
    public List<String> viewPasswords() {
        List<String> plTitles = new ArrayList<>();
        for (PasswordLog pl : pm) {
            plTitles.add(pl.getTitle());
        }
        return plTitles;
    }

    // REQUIRES: order is one of: "alphabetical", "reverse"
    // EFFECTS: returns all titles of saved passwords sorted either alphabetically or reverse alphabetically, depending
                // on order
    public List<String> viewPasswordsSorted(String order) {
        List<String> plTitles = new ArrayList<>();
        for (PasswordLog pl : pm) {
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
}
