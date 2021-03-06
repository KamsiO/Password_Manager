package ui.gui;

import model.Password;
import model.PasswordLog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controls the creation of password logs
 */

public class PasswordSaver implements ActionListener {
    private static final int MANAGER_INDEX = 2;

    private PasswordManagerPage passwordManager = PasswordApp.getPM();

    private String password;
    private PasswordLog pl;

    public void setPassword(String p) {
        password = p;
    }

    // MODIFIES: this
    // EFFECTS: creates a password log with title that user inputs
    @Override
    public void actionPerformed(ActionEvent e) {
        String title = JOptionPane.showInputDialog(null,
                "Please input title of password.",
                "Save Password",
                JOptionPane.PLAIN_MESSAGE);

        if (title != null) {
            pl = new PasswordLog(new Password(password), title);
            PasswordApp.switchTab(MANAGER_INDEX);
            passwordManager.closeLog();
            passwordManager.clearSearch();
            passwordManager.addLog(pl);
        }
    }
}
