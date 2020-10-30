package persistence;

import model.Password;
import model.PasswordLog;
import model.PasswordManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyPasswordManagerAddLog() {
        try {
            //setup
            PasswordManager pm = new PasswordManager(); //doesn't read from json before adding, so pm is empty
            PasswordLog pl = new PasswordLog(new Password("password123"), "UBC", "kamsi", "ubc.ca", "notes");
            pm.addPasswordLog(pl, "UBC");
            JsonReader reader = new JsonReader("./data/testWriterGeneralPasswordManager.json");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPasswordManager.json");
            writer.open();

            //method call
            writer.write(pm, "add", pl, "", "");
            writer.close();

            //assertions
            pm = reader.read();
            List<PasswordLog> logs = pm.getPasswordLogs();
            assertEquals(1, pm.getPasswordLogs().size());
            checkLog("password123", "UBC", "kamsi", "ubc.ca", "notes", logs.get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralPasswordManagerAddLog() {
        try {
            //setup
            PasswordManager pm;
            PasswordLog pl = new PasswordLog(new Password("abc"), "gmail", "kamsi", "gmail.com", "notes");
            JsonReader reader = new JsonReader("./data/testWriterGeneralPasswordManager.json");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPasswordManager.json");
            restoreToInitial();
            pm = reader.read();
            pm.addPasswordLog(pl, "gmail");

            //method call
            writer.open();
            writer.write(pm, "add", pl, "", "");
            writer.close();

            //assertions
            pm = reader.read();
            List<PasswordLog> logs = pm.getPasswordLogs();
            assertEquals(2, pm.getPasswordLogs().size());
            checkLog("password123", "UBC", "kamsi", "ubc.ca", "notes", logs.get(1));
            checkLog("abc", "gmail", "kamsi", "gmail.com", "notes", logs.get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralPasswordManagerDeleteLog() {
        try {
            //setup
            PasswordManager pm;
            PasswordLog pl = new PasswordLog(new Password("password123"), "UBC", "kamsi", "ubc.ca", "notes");
            JsonReader reader = new JsonReader("./data/testWriterGeneralPasswordManager.json");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPasswordManager.json");
            restoreToInitial();
            pm = reader.read();
            //pm.addPasswordLog(pl, "gmail");

            //method call
            writer.open();
            writer.write(pm, "delete", pl, "", "");
            writer.close();

            //assertions
            pm = reader.read();
            assertEquals(0, pm.getPasswordLogs().size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralPasswordManagerUpdateLog() {
        try {
            //setup
            PasswordManager pm;
            PasswordLog pl = new PasswordLog(new Password("password123"), "UBC", "kamsi", "ubc.ca", "notes");
            JsonReader reader = new JsonReader("./data/testWriterGeneralPasswordManager.json");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralPasswordManager.json");
            restoreToInitial();
            pm = reader.read();

            //method call
            writer.open();
            writer.write(pm, "update", pl, "password", "newpassword");
            writer.close();
            writer.open();
            writer.write(pm, "update", pl, "username", "aPerson");
            writer.close();
            writer.open();
            writer.write(pm, "update", pl, "url", "new.com");
            writer.close();
            writer.open();
            writer.write(pm, "update", pl, "notes", "new notes");
            writer.close();
            writer.open();
            writer.write(pm, "update", pl, "title", "New");
            writer.close();

            //assertions
            pm = reader.read();
            List<PasswordLog> logs = pm.getPasswordLogs();
            assertEquals(1, pm.getPasswordLogs().size());
            checkLog("newpassword", "New", "aPerson", "new.com", "new notes", logs.get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    private void restoreToInitial() {
        JsonWriter writer = new JsonWriter("./data/testWriterGeneralPasswordManager.json");
        PasswordManager pm = new PasswordManager();
        PasswordLog pl = new PasswordLog(new Password("password123"), "UBC", "kamsi", "ubc.ca", "notes");
        pm.addPasswordLog(pl, "UBC");
        try {
            writer.open();
            writer.writeForTest(pm);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JSONObject manager = pm.toJson();
        JSONArray logs = manager.getJSONArray("passwordLogs");
        assertEquals(1, logs.length());
    }
}
