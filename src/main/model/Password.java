package model;

import com.mifmif.common.regex.Generex;

import java.util.*;
import java.util.regex.Pattern;

/**
 * A representation of a password and its strength which depends on the password's length and inclusion of at least
 * one of each: a lowercase, uppercase, digit, and special character. The password can be generated, or manually set.
 */

public class Password {
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 12;
    private String pw;
    private String strengthRating;
    private Boolean length;
    private Boolean hasLowercase;
    private Boolean hasUppercase;
    private Boolean hasDigit;
    private Boolean hasSpecialChar;

    // EFFECTS: creates an empty password that does not meet any requirements
    public Password() {
        pw = "";
        length = false;
        hasLowercase = false;
        hasUppercase = false;
        hasDigit = false;
        hasSpecialChar = false;
    }

    // EFFECTS: creates a password from inputted string with strength depending on features of inputted string
    public Password(String pw) {
        setPassword(pw);
    }

    // MODIFIES: this
    // EFFECTS: generates and sets password to a string that is MIN_PASSWORD_LENGTH - MAX_PASSWORD_LENGTH characters
                // long and has all the requirements of a strong password
    public void generateStrongPassword() {
        Generex generex = new Generex("[0-9]+[A-Z]+[a-z]+[!\"#$%&'()*+,-./:;=?@^_{}~]{1,3}");
        String stringFromRegex = generex.random(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH); // creates a string of length
            // between MIN_PASSWORD_LENGTH and MAX_PASSWORD_LENGTH made up of characters from generex regular expression
        List<String> characters = Arrays.asList(stringFromRegex.split("")); // splits string into chars and adds
                                                                            // them to list
        StringBuilder password = new StringBuilder();
        Collections.shuffle(characters);
        for (String character : characters) {
            password.append(character);
        }
        pw = password.toString();
        checkPasswordStrength();
    }

    // MODIFIES: this
    // EFFECTS: checks if p is long enough, has a lowercase character, has an uppercase character, has a digit, and
    //              has a special char
    private void checkPasswordStrength() { //https://www.tutorialspoint.com/java/java_regular_expressions.htm
        Pattern capital = Pattern.compile("[A-Z]");
        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern specialChar = Pattern.compile("[!\"#$%&'()*+,-./:;=?@^_{}~]");

        length = pw.length() >= MIN_PASSWORD_LENGTH;
        hasUppercase = capital.matcher(pw).find();
        hasLowercase = lowercase.matcher(pw).find();
        hasDigit = digit.matcher(pw).find();
        hasSpecialChar = specialChar.matcher(pw).find();

        setPasswordStrengthRating();
    }

    // MODIFIES: this
    // EFFECTS: sets password to given string, and updates password strength for new password
    public void setPassword(String s) {
        pw = s;
        checkPasswordStrength();
    }

    // EFFECTS: returns password
    public String getPassword() {
        return pw;
    }

    // EFFECTS: returns a list of which requirements the password passed and failed
                // in order of: length, lowercase, uppercase, digit, and special character
    public List<Boolean> getPasswordStrength() {
        List<Boolean> strength = new ArrayList<>();
        strength.add(this.length);
        strength.add(this.hasLowercase);
        strength.add(this.hasUppercase);
        strength.add(this.hasDigit);
        strength.add(this.hasSpecialChar);
        return strength;
    }

    // MODIFIES: this
    // EFFECTS: sets the strength rating of the password based on how many requirements it meets
    private void setPasswordStrengthRating() {
        int rating = 0;
        if (length) {
            rating++;
        }
        if (hasLowercase) {
            rating++;
        }
        if (hasUppercase) {
            rating++;
        }
        if (hasDigit) {
            rating++;
        }
        if (hasSpecialChar) {
            rating++;
        }

        if (rating == 5) {
            strengthRating = "Strong";
        } else if (rating < 3) {
            strengthRating = "Poor";
        } else {
            strengthRating = "Medium";
        }
    }

    // EFFECTS: returns password's strength rating
    public String getPasswordStrengthRating() {
        return strengthRating;
    }
}
