package ui.consoleui;

import model.Password;

import static ui.consoleui.PasswordApp.commandInput;

/**
 * Monitors and presents information from/for the password generation section of the console ui.
 */

public class GeneratePasswordPage {

    // MODIFIES: Password, PasswordApp, PasswordManager
    // EFFECTS: generates a new password
    public static void generatePassword() {
        System.out.println("\nPASSWORD GENERATOR");
        boolean keepGenerating = true;

        while (keepGenerating) {
            Password password = new Password();
            password.generateStrongPassword();
            System.out.println("Your generated password is: " + password.getPassword());

            keepGenerating = makeGeneratePasswordSelection(password);
        }
    }

    // MODIFIES: PasswordApp, PasswordManager
    // EFFECTS: tracks what user decides to do with generated password
    private static boolean makeGeneratePasswordSelection(Password pw) {
        boolean choosing = true;
        while (choosing) {
            System.out.println("\nWould you like to: ");
            System.out.println("\t1 -> save this password?");
            System.out.println("\t2 -> generate a new password?");
            System.out.println("\t3 -> go back to main menu?");
            String gpCommand = commandInput.next();

            if (gpCommand.equals("2")) {
                return true;
            } else if (gpCommand.equals("1")) {
                PasswordManagerPage.savePassword(pw);
                return false;
            } else if (gpCommand.equals("3")) {
                return false;
            } else {
                System.out.println("Please select 1, 2, or 3.");
            }
        }
        return false;
    }
}
