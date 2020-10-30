package ui;

import model.PasswordLog;
import model.PasswordManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * A console app that allows user to generate passwords, save and view passwords, update stored passwords, and
 * check the strength of passwords user types in.
 */

public class PasswordApp { // ui inspired from TellerApp
    private static final String JSON_STORE = "./data/passwordmanager.json";
    public static PasswordManager passwordManager;
    public static Scanner commandInput;
    public static Scanner stringInput;
    private static JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: creates scanners to monitor user input and starts the Password app
    public PasswordApp() throws FileNotFoundException {
        commandInput = new Scanner(System.in);
        stringInput = new Scanner(System.in).useDelimiter("\n"); // post by Rajesh Samson,
            // https://stackoverflow.com/questions/39514730/how-to-take-input-as-string-with-spaces-in-java-using-scanner
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // MODIFIES: this, PasswordManager, Password, PasswordLog
    // EFFECTS: initializes the app and tracks user inputs, either taking user directly to stored passwords if there
    //              are any saved, displaying the main menu, or closing the app
    private void runApp() {
        boolean running = true;
        String command;
        init();

        if (passwordManager.viewPasswords().size() > 0) {
            PasswordManagerPage.viewPasswordManager();
        }

        while (running) {
            displayMainMenu();
            command = commandInput.next();
            if (command.equals("4")) {
                running = false;
            } else {
                processMainMenuCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS:  creates an empty password manager if store file can't be read
    //           otherwise, creates a password manager with saved passwords
    private void init() { // inspired by JsonSerializationDemo
        System.out.println("\nHello!");
        try {
            passwordManager = jsonReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
            passwordManager = new PasswordManager();
        }
    }

    // EFFECTS: displays the starting menu of options to user
    private void displayMainMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("Select from:");
        System.out.println("\t1 -> Generate New Password");
        System.out.println("\t2 -> View Stored Passwords");
        System.out.println("\t3 -> Check Password Strength");
        System.out.println("\t4 -> Quit App");
    }

    // MODIFIES: this, PasswordManager, Password, PasswordLog
    // EFFECTS: processes user command from main menu
    private void processMainMenuCommand(String command) {
        if (command.equals("1")) {
            GeneratePasswordPage.generatePassword();
        } else if (command.equals("2")) {
            PasswordManagerPage.viewPasswordManager();
        } else if (command.equals("3")) {
            CheckStrengthPage.checkPasswordStrength();
        } else {
            System.out.println("Please select 1, 2, 3, or 4.");
        }
    }

    // MODIFIES: PasswordManager
    // EFFECTS: saves the password manager to file
    public static void savePasswordManager() {
        try {
            jsonWriter.open();
            jsonWriter.write(passwordManager);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}