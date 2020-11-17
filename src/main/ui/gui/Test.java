package ui.gui;

import model.Password;
import model.PasswordLog;

import javax.swing.*;

import java.awt.*;

import static com.sun.glass.ui.Cursor.setVisible;

public class Test extends JFrame {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension MIN_SIZE = new Dimension(SCREEN_SIZE.width - (SCREEN_SIZE.width / 3),
            SCREEN_SIZE.height - (SCREEN_SIZE.height / 3));

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        doIt();
    }

    public void doIt() {
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(MIN_SIZE);


        setLocationRelativeTo(null);

        Container con = getContentPane();

        con.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 0.7;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;

        con.add(new PasswordLogContainer(new PasswordLog(new Password("Yochi2001!"), "gmail")));

        //pack();

        setVisible(true);
    }
}
