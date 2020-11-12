package ui.gui;

import javax.swing.*;
import java.awt.*;

public class PasswordApp extends JFrame { //https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
    private JPanel pages;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension MIN_SIZE = new Dimension(SCREEN_SIZE.width - (SCREEN_SIZE.width / 3),
                SCREEN_SIZE.height - (SCREEN_SIZE.height / 3));

    public static void main(String[] args) {
        new PasswordApp();
    }

    public PasswordApp() {
        initializeGraphics();
    }

    public void addComponentToPane(Container pane) {
        JPanel card1 = new GeneratePasswordPage();

        //JPanel card2 = new StrengthCheckerPage();

        //Create the panel that contains the "cards".
        pages = new JPanel(new CardLayout());
        pages.add(card1);
        //pages.add(card2);

        pane.add(pages, BorderLayout.CENTER);
    }

    public void initializeGraphics() {
        //JFrame frame = new JFrame("Password Manager");
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(MIN_SIZE);

        //frame.setResizable(false);
        //ImageIcon icon = new ImageIcon("");
        //frame.setIconImage(icon.getImage());

        setLocationRelativeTo(null);

        addComponentToPane(getContentPane());

        //pack();
        setVisible(true);
    }
}
