package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordManagerTest {
    PasswordManager pm;

    @BeforeEach
    public void setup() {
        pm = new PasswordManager();
    }

    @Test
    public void testAddPasswordLogNoPasswordWithTitle() {
        Password pw = new Password();

        boolean addedPassword = pm.addPasswordLog(pw, "Facebook");

        assertTrue(addedPassword);
        assertEquals(1, pm.viewPasswords().size());
        assertEquals("Facebook", pm.viewPasswords().get(0));
    }

    @Test
    public void testAddPasswordLogNoPasswordWithTitleOfSameCase() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        pm.addPasswordLog(pw1, "GMail");

        boolean addedPassword = pm.addPasswordLog(pw2, "Gmail");

        assertTrue(addedPassword);
        assertEquals(2, pm.viewPasswords().size());
    }

    @Test
    public void testAddPasswordLogPasswordWithTitleExists() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        pm.addPasswordLog(pw1, "Snapchat");

        boolean addedPassword = pm.addPasswordLog(pw2, "Snapchat");

        assertFalse(addedPassword);
        assertEquals(1, pm.viewPasswords().size());
    }

    @Test
    public void testDeletePasswordLog() {
        Password pw = new Password();
        pm.addPasswordLog(pw, "Snapchat");

        pm.deletePasswordLog("Snapchat");

        assertEquals(0, pm.viewPasswords().size());
    }

    @Test
    public void testDeletePasswordLogPasswordLogsWithSimilarNames() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        pm.addPasswordLog(pw1, "Snapchat");
        pm.addPasswordLog(pw2, "Snapchat 2");

        pm.deletePasswordLog("Snapchat");

        assertEquals(1, pm.viewPasswords().size());
        assertEquals("Snapchat 2", pm.viewPasswords().get(0));
    }

    @Test
    public void testGetPasswordLogPasswordLogsWithSimilarNames() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        pm.addPasswordLog(pw1, "Insta");
        pm.addPasswordLog(pw2, "insta");

        PasswordLog pl = pm.getPasswordLog("Insta");

        assertEquals("Insta", pl.getTitle());
    }

    @Test
    public void testViewPasswordsSortedAlphabetically() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        Password pw3 = new Password();
        pm.addPasswordLog(pw1, "Microsoft");
        pm.addPasswordLog(pw2, "Apple ID");
        pm.addPasswordLog(pw3, "Amazon");

        List<String> passwords = pm.viewPasswordsSorted("alphabetical");

        assertEquals("Amazon", passwords.get(0));
        assertEquals("Apple ID", passwords.get(1));
        assertEquals("Microsoft", passwords.get(2));
    }

    @Test
    public void testViewPasswordsSortedReversAlphabetically() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        Password pw3 = new Password();
        pm.addPasswordLog(pw1, "Microsoft");
        pm.addPasswordLog(pw2, "Apple ID");
        pm.addPasswordLog(pw3, "Amazon");

        List<String> passwords = pm.viewPasswordsSorted("reverse");

        assertEquals("Microsoft", passwords.get(0));
        assertEquals("Apple ID", passwords.get(1));
        assertEquals("Amazon", passwords.get(2));
    }
}
