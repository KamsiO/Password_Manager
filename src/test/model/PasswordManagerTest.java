package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordManagerTest {
    private PasswordManager pm;

    @BeforeEach
    public void setup() {
        pm = new PasswordManager();
    }

    @Test
    public void testAddPasswordLogNoPasswordWithTitle() {
        Password pw = new Password();
        PasswordLog pl = new PasswordLog(pw, "UBC");

        boolean addedPassword1 = pm.addPasswordLog(pw, "Facebook");
        boolean addedPassword2 = pm.addPasswordLog(pl, "UBC");

        assertTrue(addedPassword1);
        assertTrue(addedPassword2);
        assertEquals(2, pm.getPasswordLogs().size());
        assertEquals("UBC", pm.viewPasswords().get(0));
        assertEquals("Facebook", pm.viewPasswords().get(1));
    }

    @Test
    public void testAddPasswordLogNoPasswordWithTitleOfSameCase() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        pm.addPasswordLog(pw1, "GMail");
        PasswordLog pl = new PasswordLog(pw2, "gmail");

        boolean addedPassword1 = pm.addPasswordLog(pw2, "Gmail");
        boolean addedPassword2 = pm.addPasswordLog(pl, "gmail");

        assertTrue(addedPassword1);
        assertTrue(addedPassword2);
        assertEquals(3, pm.getPasswordLogs().size());
    }

    @Test
    public void testAddPasswordLogPasswordWithTitleExists() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        pm.addPasswordLog(pw1, "Snapchat");
        PasswordLog pl = new PasswordLog(pw2, "Snapchat");

        boolean addedPassword1 = pm.addPasswordLog(pw2, "Snapchat");
        boolean addedPassword2 = pm.addPasswordLog(pl, "Snapchat");

        assertFalse(addedPassword1);
        assertFalse(addedPassword2);
        assertEquals(1, pm.getPasswordLogs().size());
    }

    @Test
    public void testDeletePasswordLog() {
        Password pw = new Password();
        pm.addPasswordLog(pw, "Snapchat");

        pm.deletePasswordLog("Snapchat");

        assertEquals(0, pm.getPasswordLogs().size());
    }

    @Test
    public void testDeletePasswordLogPasswordLogsWithSimilarNames() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        pm.addPasswordLog(pw1, "Snapchat");
        pm.addPasswordLog(pw2, "Snapchat 2");

        pm.deletePasswordLog("Snapchat");

        assertEquals(1, pm.getPasswordLogs().size());
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

    @Test
    public void searchPasswordsManyFound() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        Password pw3 = new Password();
        pm.addPasswordLog(pw1, "Microsoft");
        pm.addPasswordLog(pw2, "Apple ID");
        pm.addPasswordLog(pw3, "Amazon");

        List<PasswordLog> searched = pm.searchPasswords("a");
        assertEquals(2, searched.size());
        assertEquals(searched.get(0).getTitle(), "Amazon");
        assertEquals(searched.get(1).getTitle(), "Apple ID");
    }

    @Test
    public void searchPasswordsMessyQuery() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        Password pw3 = new Password();
        pm.addPasswordLog(pw1, "Microsoft");
        pm.addPasswordLog(pw2, "Apple ID");
        pm.addPasswordLog(pw3, "Amazon");

        List<PasswordLog> searched = pm.searchPasswords("ApPle id  ");
        assertEquals(1, searched.size());
        assertEquals(searched.get(0).getTitle(), "Apple ID");
    }

    @Test
    public void searchPasswordsNoneFound() {
        Password pw1 = new Password();
        Password pw2 = new Password();
        Password pw3 = new Password();
        pm.addPasswordLog(pw1, "Microsoft");
        pm.addPasswordLog(pw2, "Apple ID");
        pm.addPasswordLog(pw3, "Amazon");

        List<PasswordLog> searched = pm.searchPasswords("Google");
        assertEquals(0, searched.size());
    }
}
