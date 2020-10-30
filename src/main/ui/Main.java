package ui;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new PasswordApp();
        } catch (IOException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
