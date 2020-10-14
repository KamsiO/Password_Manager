package ui;

import model.Password;

import java.util.List;

import static ui.PasswordApp.commandInput;
import static ui.PasswordApp.stringInput;

/**
 * Monitors and presents information from/for the password strength checker section of the console ui.
 */

public class CheckStrengthPage {
    // MODIFIES: Password
    // EFFECTS: shows the strength attributes of the password that user inputs
    public static void checkPasswordStrength() {
        System.out.println("\nPASSWORD STRENGTH CHECKER");
        System.out.println("Please type in the password you would like to check.");
        String inputtedPassword = stringInput.next();
        Password password = new Password();
        password.setPassword(inputtedPassword);
        String rating = password.getPasswordStrengthRating();
        List<Boolean> requirements = password.getPasswordStrength();
        System.out.println("\nThe strength of \"" + inputtedPassword + "\" is: " + rating);
        System.out.println("Here are the requirements it passed:");
        System.out.println("\tLong enough: " + requirements.get(0));
        System.out.println("\tHas a lowercase letter: " + requirements.get(1));
        System.out.println("\tHas an uppercase letter: " + requirements.get(2));
        System.out.println("\tHas a digit: " + requirements.get(3));
        System.out.println("\tHas a special character: " + requirements.get(4));

        checkOrSave(password);
    }

    // MODIFIES: PasswordApp, PasswordManager, Password
    // EFFECTS: allows user to save checked password or check another password
    private static void checkOrSave(Password pw) {
        boolean answering = true;
        while (answering) {
            System.out.println("\nWould you like to:");
            System.out.println("\t1 -> save this password?");
            System.out.println("\t2 -> check another password?");
            System.out.println("\t3 -> go back to main menu?");
            String answer = commandInput.next();
            if (answer.equals("1")) {
                PasswordManagerPage.savePassword(pw);
                answering = false;
            } else if (answer.equals("2")) {
                checkPasswordStrength();
                answering = false;
            } else if (answer.equals("3")) {
                answering = false;
            } else {
                System.out.println("Please select 1, 2, or 3.");
            }
        }
    }
}
