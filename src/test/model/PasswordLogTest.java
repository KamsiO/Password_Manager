package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordLogTest {
    private PasswordLog pl;

    @BeforeEach
    public void setup() {
        Password pw = new Password();
        pl = new PasswordLog(pw, "UBC");
    }

    @Test
    public void testOverloadedConstructor() {
        Password pw = new Password();
        PasswordLog log = new PasswordLog(pw, "UBC", "kamsi", "ubc.ca", "mom's birthday");
        assertEquals(pw, log.getPassword());
        assertEquals("UBC", log.getTitle());
        assertEquals("ubc.ca", log.getURL());
        assertEquals("kamsi", log.getUsername());
        assertEquals("mom's birthday", log.getNotes());
    }

    @Test
    public void testSetNotes(){
        pl.setNotes("Security q is my b-day");
        String notes = pl.getNotes();
        assertEquals("Security q is my b-day", notes);
    }

    @Test
    public void testSetURL(){
        pl.setURL("https://ssc.adm.ubc.ca/sscportal/");
        String url = pl.getURL();
        assertEquals("https://ssc.adm.ubc.ca/sscportal/", url);
    }

    @Test
    public void testSetUsername(){
        pl.setUsername("student");
        String username = pl.getUsername();
        assertEquals("student", username);
    }

    @Test
    public void testUpdatePassword(){
        Password newPW = new Password();
        newPW.setPassword("password123");
        pl.updatePassword(newPW);
        Password password = pl.getPassword();
        assertEquals("password123", password.getPassword());
    }

    @Test
    public void testUpdateTitle(){
        pl.updateTitle("UBC SSC");
        String title = pl.getTitle();
        assertEquals("UBC SSC", title);
    }

}
