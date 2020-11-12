package ui.consoleui;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new PasswordApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
