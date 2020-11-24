package persistence;

import model.PasswordLog;
import model.PasswordManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest { //based off of JsonSerializationDemo

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nope.json");
        try {
            PasswordManager pm = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    void testReaderEmptyPasswordManager() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyPasswordManager.json");
        try {
            PasswordManager pm = reader.read();
            assertEquals(0, pm.viewPasswords().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralPasswordManager() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralPasswordManager.json");
        try {
            PasswordManager pm = reader.read();
            List<PasswordLog> logs = pm.getPasswordLogs();
            assertEquals(2, logs.size());
            checkLog("password123", "UBC", "kamsi", "ubc.ca", "security question is your birthday", logs.get(1));
            checkLog("abc123", "GMail", "kamsi", "gmail.com", "security question is mom's birthday", logs.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
