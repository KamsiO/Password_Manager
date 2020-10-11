package model;

import com.mifmif.common.regex.Generex;

import java.util.*;
import java.util.regex.Pattern;

/**
 * A representation of a password and its strength which depends on the password's length and inclusion of at least
 * one of each: a lowercase, uppercase, digit, and special character. The password can be generated, or manually set.
 */


public class Password {
    private String pw;
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

    // MODIFIES: this
    // EFFECTS: generates and sets password to a string that is 8-12 characters long and has all the requirements of
    //              a strong password
    public void generateStrongPassword() {
        Generex genrex = new Generex("[0-9]+[A-Z]+[a-z]+[!\"#$%&'()*+,-./:;=?@^_{}~]{1,3}");
        String stringFromRegex = genrex.random(8,12); // creates a string of length between 8-12 made up of characters
                                                        // from genrex regular expression
        List<String> characters = Arrays.asList(stringFromRegex.split("")); // splits string into chars and adds
                                                                                    // them to list
        StringBuilder password = new StringBuilder();
        Collections.shuffle(characters);
        for (String character : characters) {
            password.append(character);
        }
        pw = password.toString();
    }

    // MODIFIES: this
    // EFFECTS: checks if p is long enough, has a lowercase character, has an uppercase character, has a digit, and
    //              has a special char
    public void checkPasswordStrength() { //https://www.tutorialspoint.com/java/java_regular_expressions.htm
        Pattern capital = Pattern.compile("[A-Z]");
        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern specialChar = Pattern.compile("[!\"#$%&'()*+,-./:;=?@^_{}~]");

        length = pw.length() >= 8;
        hasUppercase = capital.matcher(pw).find();
        hasLowercase = lowercase.matcher(pw).find();
        hasDigit = digit.matcher(pw).find();
        hasSpecialChar = specialChar.matcher(pw).find();
    }

    // MODIFIES: this
    // EFFECTS: sets password to given string, and updates password strength for new password
    public void setPassword(String s) {
        pw = s;
        this.checkPasswordStrength();
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
}
