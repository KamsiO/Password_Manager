package ui;

import model.Password;
import model.PasswordLog;
import model.PasswordManager;

import java.util.List;

import static ui.PasswordApp.passwordManager;
import static ui.PasswordApp.commandInput;
import static ui.PasswordApp.stringInput;

/**
 * Monitors and presents information from/for the password manager section of the console ui.
 */

public class PasswordManagerPage {

    // MODIFIES: PasswordApp, PasswordManager, Password, PasswordLog
    // EFFECTS: shows stored passwords and presents user with commands for the password manager
    public static void viewPasswordManager() {
        System.out.println("\nPASSWORD MANAGER");
        if (passwordManager.viewPasswords().isEmpty()) {
            chooseFromEmptyPasswordManager();
        } else {
            printPasswords(passwordManager.viewPasswords());
            chooseFromContainsPasswordManager();
        }
    }

    // MODIFIES: PasswordApp, PasswordManager, Password
    // EFFECTS: presents commands and tracks user input if the user has no saved passwords
    private static void chooseFromEmptyPasswordManager() {
        boolean choosingFromFalse = true;
        while (choosingFromFalse) {
            System.out.println("You do not have any saved passwords yet.");
            System.out.println("\nWould you like to:");
            System.out.println("\t1 -> add a new password?");
            System.out.println("\t2 -> go back to main menu?");
            String answer = commandInput.next();
            if (answer.equals("1")) {
                addNewPassword();
                choosingFromFalse = false;
                viewPasswordManager();
            } else if (answer.equals("2")) {
                choosingFromFalse = false;
            } else {
                System.out.println("Please select 1 or 2.");
            }
        }
    }

    // MODIFIES: PasswordApp, PasswordManager, Password, PasswordLog
    // EFFECTS: presents user with commands if they have saved passwords
    private static void chooseFromContainsPasswordManager() {
        boolean choosingFromTrue = true;
        while (choosingFromTrue) {
            System.out.println("\nWould you like to:");
            System.out.println("\t1 -> sort passwords alphabetically?");
            System.out.println("\t2 -> sort passwords reverse alphabetically?");
            System.out.println("\t3 -> view a stored password?");
            System.out.println("\t4 -> add a new password?");
            System.out.println("\t5 -> delete a stored password?");
            System.out.println("\t6 -> go back to main menu?");
            String answer = commandInput.next();

            choosingFromTrue = handlePMContainsCommand(answer);
        }
    }

    // MODIFIES: PasswordApp, PasswordManager, Password, PasswordLog
    // EFFECTS: tracks user input if the user has saved passwords
    private static boolean handlePMContainsCommand(String answer) {
        if (answer.equals("1")) {
            printPasswords(passwordManager.viewPasswordsSorted("alphabetical"));
            return true;
        } else if (answer.equals("2")) {
            printPasswords(passwordManager.viewPasswordsSorted("reverse"));
            return true;
        } else if (answer.equals("3")) {
            getPasswordLog();
            return false;
        } else if (answer.equals("4")) {
            addNewPassword();
            viewPasswordManager();
            return false;
        } else if (answer.equals("5")) {
            deletePassword();
            viewPasswordManager();
            return false;
        } else if (answer.equals("6")) {
            return false;
        } else {
            System.out.println("Please select 1, 2, 3, 4, 5, or 6.");
            return true;
        }
    }

    // MODIFIES: PasswordApp, PasswordManager
    // EFFECTS: adds a new password log to the password manager under the name that user inputs if a password with
    // that name does not already exist
    public static void savePassword(Password pw) {
        System.out.println("Please type the name you would like to save the password under.");
        boolean savingPassword = true;
        while (savingPassword) {
            String title = stringInput.next();
            boolean added = passwordManager.addPasswordLog(pw, title);
            if (added) {
                System.out.println("Password successfully saved under \"" + title + "\"!");
                PasswordApp.savePasswordManager();
                savingPassword = false;
            } else {
                System.out.println("A password log with name \""
                        + title + "\" already exists. Please input another name.");
            }
        }
    }

    // EFFECTS: prints out the titles of all the saved passwords
    private static void printPasswords(List<String> passwords) {
        System.out.println("Your stored passwords are:");
        for (String p : passwords) {
            System.out.println("\t" + p);
        }
    }

    // MODIFIES: PasswordApp, PasswordManager, Password
    // EFFECTS: adds a new generated or inputted password to saved passwords
    private static void addNewPassword() {
        boolean choosing = true;
        Password newPassword = new Password();
        while (choosing) {
            System.out.println("\nOkay. Would you like to:");
            System.out.println("\t1 -> generate a password?");
            System.out.println("\t2 -> type in a password?");
            String answer = commandInput.next();
            if (answer.equals("1")) {
                newPassword.generateStrongPassword();
                PasswordManagerPage.savePassword(newPassword);
                choosing = false;
            } else if (answer.equals("2")) {
                System.out.println("Please type in the password you would like to save.");
                String pw = stringInput.next();
                newPassword.setPassword(pw);
                PasswordManagerPage.savePassword(newPassword);
                choosing = false;
            } else {
                System.out.println("Please select 1 or 2.");
            }
        }
    }

    // MODIFIES: PasswordApp, PasswordManager
    // EFFECTS: deletes the saved password under the name that user inputs, if it exists
    private static void deletePassword() {
        System.out.println("\nPlease type in the name of the password you would like to delete.");
        boolean deletingPassword = true;
        while (deletingPassword) {
            String title = stringInput.next();
            if (passwordManager.viewPasswords().contains(title)) {
                passwordManager.deletePasswordLog(title);
                System.out.println("Password saved under under \"" + title + "\" has successfully been deleted.");
                PasswordApp.savePasswordManager();
                deletingPassword = false;
            } else {
                System.out.println("A password log with name \""
                        + title + "\" does not exist. Please input another name.");
            }
        }
    }

    // MODIFIES: PasswordLog, PasswordApp, PasswordManager
    // EFFECTS: selects the password log with name name that user inputs, if it exists
    private static void getPasswordLog() {
        System.out.println("\nPlease type in the name of the password you would like to view.");
        boolean viewingPassword = true;
        while (viewingPassword) {
            String title = stringInput.next();
            if (passwordManager.viewPasswords().contains(title)) {
                PasswordLog pl = passwordManager.getPasswordLog(title);
                viewingPassword = false;
                PasswordLogPage.viewPasswordLog(pl);
            } else {
                System.out.println("A password log with name \""
                        + title + "\" does not exist. Please input another name.");
            }
        }
    }
}
