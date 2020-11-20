package ui.gui;

import exceptions.ObjectNotFoundException;
import model.PasswordLog;
import model.PasswordManager;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PasswordManagerPage extends JPanel {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private PasswordManager pm;
    private List<PasswordLogContainer> logs;
    //private JPanel logDisplayArea;
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

    public PasswordManagerPage() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        visited = false;
    }

    public void checkShouldLoad() {
        if (!visited) {
            try {
                PasswordManager loaded = jsonReader.read();
                int load = 1;
                if (loaded.getPasswordLogs().size() > 0) {
                    load = JOptionPane.showConfirmDialog(null,
                            "Would you like to load saved passwords? Choosing no then saving later will overwrite previously saved passwords.",
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

    public void addLog(PasswordLog log) {
        pm.addPasswordLog(log, log.getTitle());
        savePasswordManager("add", log, "", "");
        //https://stackoverflow.com/questions/2510159/can-i-add-a-component-to-a-specific-grid-cell-when-a-gridlayout-is-used
        passwordContainer.add(new PasswordLogContainer(log), 0, 0);
    }

    public void deleteLog(PasswordLogContainer log) {
        savePasswordManager("delete", log.getLog(), "", "");
        pm.deletePasswordLog(log.getLog().getTitle());
        passwordContainer.remove(log);
        repaint();
    }

    public void viewLog(PasswordLogContainer container) {
        logPage = new PasswordLogPage(container);
        add(logPage, "log");

        CardLayout cl = (CardLayout)(getLayout());
        cl.show(this, "log");
    }

    public void closeLog() {
        CardLayout cl = (CardLayout)(getLayout());
        cl.show(this, "main");
    }


    private void initializeLogs() {
        logs = new ArrayList<>();
        for (PasswordLog pl : pm.getPasswordLogs()) {
            logs.add(new PasswordLogContainer(pl));
        }
    }

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

    private void displayLogs() {
        JPanel logDisplayArea = makeLogDisplayArea();
        JScrollPane scrollable = new JScrollPane(logDisplayArea);
        scrollable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollable.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 0)));
        //scrollable.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPage.add(scrollable);
    }

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

//        passwordContainer.add(new PasswordLogContainer(new PasswordLog(new Password("testing978"), "gmail")));
//        for (int i = 0; i < 3; i++) {
//            passwordContainer.add(new PasswordLogContainer(new PasswordLog(new Password("password123"), "facebook")));
//        }
        return passwordContainer;
    }

    private JPanel makeHeader() {
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout());
        //Dimension min = new Dimension(SCREEN_SIZE.width - (SCREEN_SIZE.width / 3), SCREEN_SIZE.height / 100);
        //header.setPreferredSize(min);
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
        //search.addKeyListener(this);
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
}
