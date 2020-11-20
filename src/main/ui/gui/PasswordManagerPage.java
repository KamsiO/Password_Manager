package ui.gui;

import exceptions.ObjectNotFoundException;
import model.PasswordLog;
import model.PasswordManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The JPanel that shows and controls all features related to the password manager.
 */

public class PasswordManagerPage extends JPanel {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private PasswordManager pm;
    private List<PasswordLogContainer> logs;
    private JPanel passwordContainer;

    private JPanel mainPage;
    private JPanel logPage;

    private static final String JSON_STORE = "./data/passwordmanager.json";
    private static JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Boolean visited;

    private final String[] saveOptions = {"Generate", "Input", "Cancel"};
    private static final int GENERATE_INDEX = 0;
    private static final int INPUT_INDEX = 1;

    // EFFECTS: creates a new password manager page and sets up its save and load features
    public PasswordManagerPage() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        visited = false;
    }

    // MODIFIES: this
    // EFFECTS: checks if saved passwords should be loaded when user first visits this
    public void checkShouldLoad() {
        if (!visited) {
            try {
                PasswordManager loaded = jsonReader.read();
                int load = 1;
                if (loaded.getPasswordLogs().size() > 0) {
                    load = JOptionPane.showConfirmDialog(null,
                            "Load saved passwords? Choosing no then saving overwrites previously saved passwords.",
                            "Load?",
                            JOptionPane.YES_NO_OPTION);
                }
                if (load == 0) {
                    pm = loaded;
                } else {
                    pm = new PasswordManager();
                }
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
                pm = new PasswordManager();
            }
            initializeLogs();
            initializeGraphics();
            visited = true;
        }
    }

    // MODIFIES: this, PasswordManager
    // EFFECTS: adds password log log to the password manager if a password with that name does not already exist
    public boolean addLog(PasswordLog log) {
        boolean added = pm.addPasswordLog(log, log.getTitle());
        if (added) {
            savePasswordManager("add", log, "", "");
            //https://stackoverflow.com/questions/2510159/can-i-add-a-component-to-a-specific-grid-cell-when-a-gridlayout-is-used
            passwordContainer.add(new PasswordLogContainer(log), 0, 0);
            return true;
        } else {
            JOptionPane.showConfirmDialog(null,
                    "A password with that title already exists. Please use another name.",
                    "Warning!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    // MODIFIES: this, PasswordManager
    // EFFECTS: deletes password log log
    public void deleteLog(PasswordLogContainer log) {
        savePasswordManager("delete", log.getLog(), "", "");
        pm.deletePasswordLog(log.getLog().getTitle());
        passwordContainer.remove(log);
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: creates a password log page with info from the the selected password container container then
    //          switches to that page
    public void viewLog(PasswordLogContainer container) {
        logPage = new PasswordLogPage(container);
        add(logPage, "log");

        CardLayout cl = (CardLayout)(getLayout());
        cl.show(this, "log");
    }

    // EFFECTS: switches page view back to password manager page
    public void closeLog() {
        CardLayout cl = (CardLayout)(getLayout());
        cl.show(this, "main");
    }

    public PasswordManager getPm() {
        return pm;
    }

    // MODIFIES: PasswordManager
    // EFFECTS: saves password manager to json
    public void savePasswordManager(String function, PasswordLog pl, String info, String value) {
        try {
            jsonWriter.open();
            jsonWriter.write(pm, function, pl, info, value);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        } catch (ObjectNotFoundException e) {
            System.out.println("Unable to save changes.");
        }
    }

    // MODIFIES: this
    // EFFECTS:
    private void initializeLogs() {
        logs = new ArrayList<>();
        for (PasswordLog pl : pm.getPasswordLogs()) {
            logs.add(new PasswordLogContainer(pl));
        }
    }

    // MODIFIES: this
    // EFFECTS: creates and add the components for the password manager page
    private void initializeGraphics() {
        setLayout(new CardLayout(0, 0));

        mainPage = new JPanel();
        mainPage.setLayout(new BoxLayout(mainPage, BoxLayout.PAGE_AXIS));
        mainPage.setBackground(Color.WHITE);

        JPanel header = makeHeader();
        mainPage.add(header);
        mainPage.add(Box.createRigidArea(new Dimension(0,40)));

        displayLogs();

        add(mainPage, "main");
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the area where saved passwords are displayed
    private void displayLogs() {
        JPanel logDisplayArea = makeLogDisplayArea();
        JScrollPane scrollable = new JScrollPane(logDisplayArea);
        scrollable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollable.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 0)));
        mainPage.add(scrollable);
    }

    // MODIFIES: this
    // EFFECTS: adds saved passwords to a panel
    private JPanel makeLogDisplayArea() {
        passwordContainer = new JPanel();
        GridLayout grid = new GridLayout(0, 3);
        grid.setHgap(SCREEN_SIZE.width / 50);
        grid.setVgap(SCREEN_SIZE.height / 45);
        passwordContainer.setLayout(grid);
        passwordContainer.setBackground(Color.WHITE);
        passwordContainer.setBorder(BorderFactory.createEmptyBorder(50, 100, 80, 100));

        for (PasswordLogContainer log : logs) {
            passwordContainer.add(log);
        }

        return passwordContainer;
    }

    // EFFECTS: creates the header for the password manager page
    private JPanel makeHeader() {
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Manager   ");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("", Font.BOLD, SCREEN_SIZE.width / 25));
        header.add(title);

        JTextField search = new JTextField();
        search.setFont(new Font("", Font.PLAIN, 50));
        search.setHorizontalAlignment(JTextField.CENTER);
        Dimension fieldSize = new Dimension(SCREEN_SIZE.width / 3, SCREEN_SIZE.height / 26);
        search.setPreferredSize(fieldSize);
        header.add(search);

        JButton add = new JButton("New");
        add.setBackground(new Color(145, 242, 241));
        add.setPreferredSize(new Dimension(fieldSize.height + 70, fieldSize.height));
        add.setFont(new Font("", Font.ITALIC, 30));
        add.setForeground(Color.BLACK);
        add.addActionListener(evt -> chooseAddSource());
        header.add(add);

        return header;
    }

    // MODIFIES: PasswordApp
    // EFFECTS: switches tab to the generator or strength checker based on user input
    private void chooseAddSource() {
        int source = JOptionPane.showOptionDialog(null,
                "Would you like to generate or input your password?",
                "New Password",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                saveOptions,
                0);

        if (source == 0) {
            PasswordApp.getGeneratorPage().generate();
            PasswordApp.switchTab(GENERATE_INDEX);
        } else if (source == 1) {
            PasswordApp.getCheckerPage().changePageToStart();
            PasswordApp.switchTab(INPUT_INDEX);
        }
    }
}
