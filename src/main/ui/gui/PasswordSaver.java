package ui.gui;

import jdk.nashorn.internal.scripts.JO;
import model.Password;
import model.PasswordLog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordSaver implements ActionListener {
    private static final int MANAGER_INDEX = 2;
    private String password;
    private PasswordLog pl;

    public void setPassword(String p) {
        password = p;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String title = JOptionPane.showInputDialog(null,
                "Please input title of password.",
                "Save Password",
                JOptionPane.PLAIN_MESSAGE);

        //System.out.println(password);

        if (title != null) {
            PasswordApp.switchTab(MANAGER_INDEX);
        }
    }

    //public void makePl
}
