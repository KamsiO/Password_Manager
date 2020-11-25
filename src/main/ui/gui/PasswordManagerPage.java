package ui.gui;

import exceptions.ObjectNotFoundException;
import exceptions.PasswordLogDoesNotExistException;
import model.PasswordLog;
import model.PasswordManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The JPanel that shows and controls all features related to the password manager.
 */

public class PasswordManagerPage extends JPanel {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension fieldSize = new Dimension(SCREEN_SIZE.width / 3, SCREEN_SIZE.height / 26);

    private PasswordManager pm;
    private List<PasswordLogContainer> logs;

    private JPanel mainPage;
    private JPanel logPage;

    private JScrollPane scrollable;
    private JPanel passwordContainer;
    private JTextField search;
    private JButton searchButton;
    private JPanel searchedPasswordContainer;

    private static final String JSON_STORE = "./data/passwordmanager.json";
    private static JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Boolean visited;
    private Boolean searched;

    private final String[] saveOptions = {"Generate", "Input", "Cancel"};
    private static final int GENERATE_INDEX = 0;
    private static final int INPUT_INDEX = 1;

    // EFFECTS: creates a new password manager page and sets up its save and load features
    public PasswordManagerPage() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        visited = false;
        searched = false;
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
        try {
            pm.deletePasswordLog(log.getLog().getTitle());
        } catch (PasswordLogDoesNotExistException e) {
            JOptionPane.showConfirmDialog(null,
                    "A password log with that title does not exist",
                    "Warning!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE);
        }
        if (searched) {
            searchedPasswordContainer.remove(log);
            search();
        } else {
            passwordContainer.remove(log);
        }
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
            JOptionPane.showConfirmDialog(null,
                    "Unable to write to file: " + JSON_STORE,
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        } catch (ObjectNotFoundException e) {
            JOptionPane.showConfirmDialog(null,
                    "Unable to save changes.",
                    "Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a list of password log containers from the password logs that user has loaded
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
        scrollable = new JScrollPane(logDisplayArea);
        setupScrollPane();
        mainPage.add(scrollable);
    }

    // MODIFIES: this
    // EFFECTS: sets the look and behaviour of scroll pane
    private void setupScrollPane() {
        scrollable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollable.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 0)));
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

        JLabel title = new JLabel("Manager  ");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("", Font.BOLD, SCREEN_SIZE.width / 25));
        header.add(title);

        makeSearchField();
        header.add(search);

        makeSearchButton();
        header.add(searchButton);

        JButton add = new JButton("New");
        add.setBackground(new Color(145, 242, 241));
        add.setPreferredSize(new Dimension(fieldSize.height + 70, fieldSize.height));
        add.setFont(new Font("", Font.ITALIC, 30));
        add.setForeground(Color.BLACK);
        add.addActionListener(evt -> chooseAddSource());
        header.add(add);

        return header;
    }

    // MODIFIES: this
    // EFFECTS: creates the text field responsible for searching through passwords
    private void makeSearchField() {
        search = new JTextField();
        search.setFont(new Font("", Font.PLAIN, 50));
        search.setHorizontalAlignment(JTextField.CENTER);
        search.setPreferredSize(fieldSize);
        search.addKeyListener(new KeyAdapter() {
            @Override
            // MODIFIES: this
            // EFFECTS: enables the search button when user starts typing in the text field
            public void keyReleased(KeyEvent e) {
                searchButton.setEnabled(search.getText().length() > 0);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: creates the button responsible for searching through passwords
    private void makeSearchButton() {
        searchButton = new JButton();
        searchButton.setFocusable(false);
        searchButton.setBackground(Color.WHITE);
        searchButton.setEnabled(false);
        setSearchIcon();
        Dimension fieldSize = new Dimension(SCREEN_SIZE.width / 3, SCREEN_SIZE.height / 26);
        searchButton.setPreferredSize((new Dimension(fieldSize.height + 30, fieldSize.height)));
        searchButton.addActionListener(evt -> handleSearch());
    }

    // MODIFIES: this
    // EFFECTS: sets the icon of the search button based on whether the user is searching
    private void setSearchIcon() {
        ImageIcon image;
        if (searched) {
            image = new ImageIcon("data/x.png");
        } else {
            image = new ImageIcon("data/search.png");
        }
        searchButton.setIcon(image);
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

    // MODIFIES: this
    // EFFECTS: if user has not searched before, shows search, otherwise clears current search
    private void handleSearch() {
        if (searched) {
            clearSearch();
        } else {
            search();
        }
    }

    // MODIFIES: this
    // EFFECTS: shows the password logs that have titles that contain the string in search bar or none
    private void search() {
        List<PasswordLog> searched = pm.searchPasswords(search.getText());
        searchedPasswordContainer = new JPanel();
        searchedPasswordContainer.setBackground(Color.WHITE);
        if (searched.size() == 0) {
            JLabel msg = new JLabel("No passwords with that name were found.");
            msg.setFont(new Font("", Font.ITALIC, 50));
            searchedPasswordContainer.add(msg);
        } else {
            List<PasswordLogContainer> containers = new ArrayList<>();
            for (PasswordLog log : searched) {
                containers.add(new PasswordLogContainer(log));
            }
            GridLayout grid = new GridLayout(0, 3);
            grid.setHgap(SCREEN_SIZE.width / 50);
            grid.setVgap(SCREEN_SIZE.height / 45);
            searchedPasswordContainer.setLayout(grid);
            searchedPasswordContainer.setBackground(Color.WHITE);
            searchedPasswordContainer.setBorder(BorderFactory.createEmptyBorder(50, 100, 80, 100));

            for (PasswordLogContainer log : containers) {
                searchedPasswordContainer.add(log);
            }
        }
        showSearch(searchedPasswordContainer);
    }

    // MODIFIES: this
    // EFFECTS: changes the scroll panel to show only the password logs that were queried, if any
    private void showSearch(JPanel panel) {
        mainPage.remove(scrollable);

        scrollable = new JScrollPane(panel);
        setupScrollPane();
        mainPage.add(scrollable);

        searched = true;
        search.setEnabled(false);
        setSearchIcon();

        repaint();
        revalidate();
    }

    // MODIFIES: this
    // EFFECTS: clears the search by setting the search button, search bar, and displayed logs back to normal
    public void clearSearch() {
        mainPage.remove(scrollable);
        initializeLogs();
        scrollable = new JScrollPane(makeLogDisplayArea());
        setupScrollPane();
        mainPage.add(scrollable);

        searched = false;
        search.setText("");
        searchButton.setEnabled(false);
        search.setEnabled(true);
        setSearchIcon();

        repaint();
        revalidate();
    }
}
