package ui;

import model.Password;
import model.PasswordLog;

import static ui.PasswordApp.passwordManager;
import static ui.PasswordApp.commandInput;
import static ui.PasswordApp.stringInput;

/**
 * Monitors and presents information from/for a password log selected from the password manager section.
 */

public class PasswordLogPage {

    // MODIFIES: Password, PasswordLog
    // EFFECTS: prints out the password and its related info
    public static void viewPasswordLog(PasswordLog pl) {
        System.out.println("\nName: " + pl.getTitle());
        System.out.println("Password: " + pl.getPassword().getPassword());
        System.out.println("Password Rating: " + pl.getPassword().getPasswordStrengthRating());
        System.out.println("Username: " + pl.getUsername());
        System.out.println("URL: " + pl.getURL());
        System.out.println("Notes: ");
        System.out.println("\t" + pl.getNotes());

        showPasswordLogCommands(pl);
    }

    // MODIFIES: Password, PasswordLog
    // EFFECTS: presents users with commands they can make on the given password log
    private static void showPasswordLogCommands(PasswordLog pl) {
        boolean choosing = true;
        while (choosing) {
            System.out.println("\nWould you like to: ");
            System.out.println("\t 1 -> update name?");
            System.out.println("\t 2 -> update password?");
            System.out.println("\t 3 -> update username?");
            System.out.println("\t 4 -> update URL?");
            System.out.println("\t 5 -> update notes?");
            System.out.println("\t 6 -> go back to saved passwords?");

            String answer = commandInput.next();
            choosing = handlePasswordLogCommands(answer, pl);
        }
    }

    // MODIFIES: Password, PasswordLog
    // EFFECTS: handles user commands on given password log
    private static boolean handlePasswordLogCommands(String a, PasswordLog pl) {
        if (a.equals("1")) {
            updateRelatedLogInfo("title", pl);
            return false;
        } else if (a.equals("2")) {
            updatePassword(pl);
            return false;
        } else if (a.equals("3")) {
            updateRelatedLogInfo("username", pl);
            return false;
        } else if (a.equals("4")) {
            updateRelatedLogInfo("url", pl);
            return false;
        } else if (a.equals("5")) {
            updateRelatedLogInfo("notes", pl);
            return false;
        } else if (a.equals("6")) {
            PasswordManagerPage.viewPasswordManager();
            return false;
        } else {
            System.out.println("Please select 1, 2, 3, 4, 5, or 6.");
            return true;
        }
    }

    // MODIFIES: PasswordLog
    // EFFECTS: updates the specified info of the given password log to user's input
    private static void updateRelatedLogInfo(String s, PasswordLog pl) {
        System.out.println("\nPlease type in what you would like it to be replaced with:");
        if (s.equals("title")) {
            updateTitle(pl);
        } else {
            String newInfo = stringInput.next();
            if (s.equals("username")) {
                pl.setUsername(newInfo);
            } else if (s.equals("url")) {
                pl.setURL(newInfo);
            } else if (s.equals("notes")) {
                pl.setNotes(newInfo);
            }
            passwordManager.updateLogInJson(pl, s, newInfo);
        }
        System.out.println("Password log successfully updated!");
        viewPasswordLog(pl);
    }

    // MODIFIES: PasswordLog
    // EFFECTS: updates the title of the password log to user's input if a password log with that name does not
                // already exist
    private static void updateTitle(PasswordLog pl) {
        boolean updatingTitle = true;
        while (updatingTitle) {
            String newTitle = stringInput.next();
            if (passwordManager.viewPasswords().contains(newTitle)) {
                System.out.println("A password log with name \""
                        + newTitle + "\" already exists. Please input another name.");
            } else {
                pl.updateTitle(newTitle);
                PasswordApp.savePasswordManager("update", pl, "title", newTitle);
                updatingTitle = false;
            }
        }
    }

    // MODIFIES: Password, PasswordLog
    // EFFECTS: updates the stored password in the password log to be a generated password or inputted password
    private static void updatePassword(PasswordLog pl) {
        boolean choosing = true;
        Password newPassword = new Password();
        while (choosing) {
            System.out.println("\nOkay. Would you like to:");
            System.out.println("\t1 -> generate a password?");
            System.out.println("\t2 -> type in a password?");
            String answer = commandInput.next();
            if (answer.equals("1")) {
                updatePasswordGenerated(newPassword, pl);
                choosing = false;
                viewPasswordLog(pl);
            } else if (answer.equals("2")) {
                updatePasswordInputted(newPassword, pl);
                choosing = false;
                viewPasswordLog(pl);
            } else {
                System.out.println("Please select 1 or 2.");
            }
        }
    }

    private static void updatePasswordGenerated(Password newPassword, PasswordLog pl) {
        newPassword.generateStrongPassword();
        pl.updatePassword(newPassword);
        PasswordApp.savePasswordManager("update", pl, "title", newPassword.getPassword());
    }

    private static void updatePasswordInputted(Password newPassword, PasswordLog pl) {
        System.out.println("Please type in the password you would like to save.");
        String pw = stringInput.next();
        newPassword.setPassword(pw);
        pl.updatePassword(newPassword);
        PasswordApp.savePasswordManager("update", pl, "password", pw);
    }
}
