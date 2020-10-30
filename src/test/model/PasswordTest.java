package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static model.Password.*;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {
    private Password pw;

    @BeforeEach
    public void setup() {
        pw = new Password();
    }

    @Test
    public void testOverloadedConstructor() {
        Password password = new Password("password123");
        assertEquals("password123", password.getPassword());
        assertEquals("Medium", password.getPasswordStrengthRating());
    }

    @Test
    public void testGenerateStrongPassword() {
        Pattern capital = Pattern.compile("[A-Z]");
        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern specialChar = Pattern.compile("[!\"#$%&'()*+,-./:;=?@^_{}~]");

        pw.generateStrongPassword();
        String generated = pw.getPassword();

        assertTrue(capital.matcher(generated).find());
        assertTrue(lowercase.matcher(generated).find());
        assertTrue(digit.matcher(generated).find());
        assertTrue(specialChar.matcher(generated).find());

        assertTrue(pw.getPassword().length() >= MIN_PASSWORD_LENGTH &&
                pw.getPassword().length() <= MAX_PASSWORD_LENGTH);
    }

    @Test
    public void testCheckPasswordStrengthPerfect() {
        pw.setPassword("1We#abcd");
        List<Boolean> strength = pw.getPasswordStrength();

        assertTrue(strength.get(0)); //length
        assertTrue(strength.get(1)); //lowercase
        assertTrue(strength.get(2)); //uppercase
        assertTrue(strength.get(3)); //digit
        assertTrue(strength.get(4)); //special char
    }

    @Test
    public void testCheckPasswordStrengthTooShort() {
        pw.setPassword("1We@abc");
        List<Boolean> strength = pw.getPasswordStrength();

        assertFalse(strength.get(0)); //length
        assertTrue(strength.get(1)); //lowercase
        assertTrue(strength.get(2)); //uppercase
        assertTrue(strength.get(3)); //digit
        assertTrue(strength.get(4)); //special char
    }

    @Test
    public void testCheckPasswordStrengthNoCapital() {
        pw.setPassword("1we(abcd34567");
        List<Boolean> strength = pw.getPasswordStrength();

        assertTrue(strength.get(0)); //length
        assertTrue(strength.get(1)); //lowercase
        assertFalse(strength.get(2)); //uppercase
        assertTrue(strength.get(3)); //digit
        assertTrue(strength.get(4)); //special char
    }

    @Test
    public void testCheckPasswordStrengthNoLowercase() {
        pw.setPassword("1WE*ABCD23");
        List<Boolean> strength = pw.getPasswordStrength();

        assertTrue(strength.get(0)); //length
        assertFalse(strength.get(1)); //lowercase
        assertTrue(strength.get(2)); //uppercase
        assertTrue(strength.get(3)); //digit
        assertTrue(strength.get(4)); //special char

    }

    @Test
    public void testCheckPasswordStrengthNoDigit() {
        pw.setPassword("We~abcdt");
        List<Boolean> strength = pw.getPasswordStrength();

        assertTrue(strength.get(0)); //length
        assertTrue(strength.get(1)); //lowercase
        assertTrue(strength.get(2)); //uppercase
        assertFalse(strength.get(3)); //digit
        assertTrue(strength.get(4)); //special char
    }

    @Test
    public void testCheckPasswordStrengthNoSpecialChar() {
        pw.setPassword("1We4abcd");
        List<Boolean> strength = pw.getPasswordStrength();

        assertTrue(strength.get(0)); //length
        assertTrue(strength.get(1)); //lowercase
        assertTrue(strength.get(2)); //uppercase
        assertTrue(strength.get(3)); //digit
        assertFalse(strength.get(4)); //special char
    }

    @Test
    public void testSetPasswordStrengthRatingPoor() {
        pw.setPassword("password");
        assertEquals("Poor", pw.getPasswordStrengthRating());
    }

    @Test
    public void testSetPasswordStrengthRatingMedium() {
        pw.setPassword("Password");
        assertEquals("Medium", pw.getPasswordStrengthRating());
    }

    @Test
    public void testSetPasswordStrengthRatingStrong() {
        pw.setPassword("Password123!");
        assertEquals("Strong", pw.getPasswordStrengthRating());
    }
}