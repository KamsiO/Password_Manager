package ui.gui;

import javax.swing.*;

import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import java.awt.*;

/**
 * An app that allows user to generate passwords, save and view passwords, update stored passwords, and
 * check the strength of passwords user types in.
 */

public class PasswordApp extends JFrame {
    private static JTabbedPane tabbedPane;

    private static PasswordManagerPage passwordManager = new PasswordManagerPage();
    private static GeneratePasswordPage generatorPage = new GeneratePasswordPage();
    private static CheckStrengthPage checkerPage = new CheckStrengthPage();

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension MIN_SIZE = new Dimension(SCREEN_SIZE.width - (SCREEN_SIZE.width / 3),
                SCREEN_SIZE.height - (SCREEN_SIZE.height / 3));

    private static final int MANAGER_INDEX = 2;

    // EFFECTS: changes the UI look and creates a password app frame with three tabs
    public PasswordApp() {
        setLookAndFeel();
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS: changes the current tab showing to the tab at index index
    public static void switchTab(int index) {
        tabbedPane.setSelectedIndex(index);
    }

    public static PasswordManagerPage getPM() {
        return passwordManager;
    }

    public static GeneratePasswordPage getGeneratorPage() {
        return generatorPage;
    }

    public static CheckStrengthPage getCheckerPage() {
        return checkerPage;
    }

    // MODIFIES: this
    // EFFECTS: creates a JFrame with 3 tabs
    private void initializeGraphics() {
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(MIN_SIZE);

        setLocationRelativeTo(null);

        addTabs(getContentPane());

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates the JPanel tabs, a generator tab, a strength checker tab, and a password manager tab
    private void addTabs(Container pane) {
        JPanel tab1 = generatorPage;
        JPanel tab2 = checkerPage;
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

    // EFFECTS: modifies the ui look and feel
    private void setLookAndFeel() {
        //http://www.java2s.com/Tutorials/Java/Swing_How_to/JTabbedPane/Change_the_colour_of_the_JTabbedPane_header.htm
        UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(1, 0,
                0, 0));
        UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource(Color.LIGHT_GRAY));
        UIManager.put("ToolTip.background", Color.WHITE);
        UIManager.put("ToolTip.border", new BorderUIResource(new LineBorder(Color.BLACK)));
        UIManager.put("ToolTip.font", new FontUIResource(new Font("", Font.PLAIN, 30)));
    }

    // MODIFIES: this
    // EFFECTS: gives this the ability to detect if the user switches to the password manager tab
    private void setupChangeDetection() {
        //https://stackoverflow.com/questions/15494878/how-to-call-a-certain-function-when-click-on-a-tab-in-java/15495053
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == MANAGER_INDEX) {
                passwordManager.checkShouldLoad();
            }
        });
    }
}
