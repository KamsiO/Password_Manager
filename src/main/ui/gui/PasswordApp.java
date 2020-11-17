package ui.gui;

import javax.swing.*;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import java.awt.*;

public class PasswordApp extends JFrame {
    private static JTabbedPane tabbedPane;

    private static PasswordManagerPage passwordManager = new PasswordManagerPage();

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension MIN_SIZE = new Dimension(SCREEN_SIZE.width - (SCREEN_SIZE.width / 3),
                SCREEN_SIZE.height - (SCREEN_SIZE.height / 3));


    public static void main(String[] args) {
        //extract a main class, have it call new PasswordApp, PA has a call to setui



        //http://www.java2s.com/Tutorials/Java/Swing_How_to/JTabbedPane/Change_the_colour_of_the_JTabbedPane_header.htm
        UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(1, 0,
                0, 0));
        UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource(Color.LIGHT_GRAY));
        UIManager.put("ToolTip.background", Color.WHITE);
        UIManager.put("ToolTip.border", new BorderUIResource(new LineBorder(Color.BLACK)));
        UIManager.put("ToolTip.font", new FontUIResource(new Font("", Font.PLAIN, 30)));
//        Dimension optionPaneSize = new Dimension(MIN_SIZE.width / 3, MIN_SIZE.height / 5);
//        UIManager.put("OptionPane.minimumSize", optionPaneSize);
//        int fontSize = optionPaneSize.width;
//        UIManager.put("OptionPane.font", new Font("", Font.PLAIN, 100));


        new PasswordApp();
    }

    public PasswordApp() {
        initializeGraphics();
    }

    public static void switchTab(int index) {
        tabbedPane.setSelectedIndex(index);
    }

    public static PasswordManagerPage getPM() {
        return passwordManager;
    }

    private void initializeGraphics() {
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(MIN_SIZE);

        //setResizable(false);
        //ImageIcon icon = new ImageIcon("");
        //frame.setIconImage(icon.getImage());
        setLocationRelativeTo(null);

        addTabs(getContentPane());
        //add(navigateRight, BorderLayout.EAST);

        //setComponentZOrder(navigateRight, 1);
        //setComponentZOrder(pages, 0);

        //pack();
        setVisible(true);
    }

    private void addTabs(Container pane) {
        JPanel tab1 = new GeneratePasswordPage();
        JPanel tab2 = new CheckStrengthPage();
        JPanel tab3 = passwordManager;

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Generate", null, tab1,
                "Generate a new password!");

        tabbedPane.addTab("Check", null, tab2,
                "Check the strength of a password");

        tabbedPane.addTab("Manage", null, tab3,
                "Manage your saved passwords");

        //https://stackoverflow.com/questions/9052784/set-size-of-tab-in-jtabbedpane
        JLabel label1 = new JLabel("Generate");
        label1.setFont(new Font("", Font.PLAIN, 40));
        tabbedPane.setTabComponentAt(0, label1);

        JLabel label2 = new JLabel("Check");
        label2.setFont(new Font("", Font.PLAIN, 40));
        tabbedPane.setTabComponentAt(1, label2);

        JLabel label3 = new JLabel("Manage");
        label3.setFont(new Font("", Font.PLAIN, 40));
        tabbedPane.setTabComponentAt(2, label3);

        setupChangeDetection();

        pane.add(tabbedPane);
    }

    private void setupChangeDetection() {
        //https://stackoverflow.com/questions/15494878/how-to-call-a-certain-function-when-click-on-a-tab-in-java/15495053
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) {
                passwordManager.checkShouldLoad();
            }
        });
    }
}
