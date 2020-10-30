package persistence;

import model.PasswordLog;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest { //based off of JsonSerializationDemo

    protected void checkLog(String pw, String title, String user, String url, String notes, PasswordLog pl) {
        assertEquals(pw, pl.getPassword().getPassword());
        assertEquals(title, pl.getTitle());
        assertEquals(user, pl.getUsername());
        assertEquals(url, pl.getURL());
        assertEquals(notes, pl.getNotes());
    }
}
