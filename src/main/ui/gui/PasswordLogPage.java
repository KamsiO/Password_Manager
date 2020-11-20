package ui.gui;

import model.Password;
import model.PasswordLog;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel that shows and controls all features related to viewing and editing saved passwords.
 */

public class PasswordLogPage extends JPanel {
    private PasswordLog passwordLog;
    private PasswordLogContainer container;

    private JPanel infoPanel;

    private JPanel title;
    private JTextField titleField;
    private JTextField userField;
    private JTextField urlField;
    private JTextField passwordField;
    private JTextArea notesArea;

    private boolean updated = false;

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    // EFFECTS: creates a password log page with information based of the password log from given
    //          password log container container
    public PasswordLogPage(PasswordLogContainer container) {
        this.container = container;
        passwordLog = container.getLog();

        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the components for viewing and editing password log information
    private void initializeGraphics() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 0, 100));

        addTitle();
        addPassword();
        addUsername();
        addURL();
        addNotes();

        add(infoPanel);

        addButtons();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a title panel containing a text field and strength rating icon
    private void addTitle() {
        title = new JPanel(new FlowLayout(FlowLayout.LEADING));
        title.setBackground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleField = new JTextField(passwordLog.getTitle());
        titleField.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,0)));
        titleField.setForeground(Color.BLACK);
        titleField.setFont(new Font("", Font.BOLD, SCREEN_SIZE.width / 25));
        title.add(titleField);
        title.add(makeStrengthIcon());
        infoPanel.add(title);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a password panel containing a password label and password text field
    private void addPassword() {
        JPanel password = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel passwordLabel = new JLabel("Password: ");
        setupLabel(password, passwordLabel);
        passwordField = new JTextField(passwordLog.getPassword().getPassword());
        setupField(password, passwordField);
        infoPanel.add(password);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a username panel containing a username label and username text field
    private void addUsername() {
        JPanel user = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel userLabel = new JLabel("Username: ");
        setupLabel(user, userLabel);
        userField = new JTextField(passwordLog.getUsername());
        setupField(user, userField);
        infoPanel.add(user);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a url panel containing a url label and url text field
    private void addURL() {
        JPanel url = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel urlLabel = new JLabel("URL: ");
        setupLabel(url, urlLabel);
        urlField = new JTextField(passwordLog.getURL());
        setupField(url, urlField);
        urlField.setForeground(new Color(33, 158, 204));
        infoPanel.add(url);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a notes panel containing a notes label and notes text area
    private void addNotes() {
        JPanel notes = new JPanel(new FlowLayout(FlowLayout.LEADING));
        notes.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel notesLabel = new JLabel("Notes: ");
        setupLabel(notes, notesLabel);
        notesArea = new JTextArea(passwordLog.getNotes(), 2, 2);
        notesArea.setForeground(Color.BLACK);
        notesArea.setBackground(new Color(245, 245, 245));
        notesArea.setFont(new Font("", Font.PLAIN, SCREEN_SIZE.width / 55));
        Dimension areaSize = new Dimension(SCREEN_SIZE.width / 2, SCREEN_SIZE.height / 10);
        notesArea.setPreferredSize(areaSize);
        notes.add(notesArea);
        infoPanel.add(notes);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a button panel containing a a close button and save changes button
    private void addButtons() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton close = new JButton("close");
        Dimension buttonSize = new Dimension(SCREEN_SIZE.width / 25, SCREEN_SIZE.height / 27);
        close.setMinimumSize(buttonSize);
        close.setForeground(new Color(106, 112, 115));
        close.setBackground(new Color(190, 199, 204));
        close.setFocusable(false);
        close.setFont(new Font("", Font.PLAIN, 40));
        close.addActionListener(evt -> closeLog());
        buttons.add(close);

        JButton save = new JButton("save changes");
        save.setMinimumSize(buttonSize);
        save.setForeground(Color.WHITE);
        save.setBackground(new Color(242, 54, 48));
        save.setFocusable(false);
        save.setFont(new Font("", Font.PLAIN, 40));
        save.addActionListener(evt -> saveChanges());
        buttons.add(save);

        add(buttons, BorderLayout.PAGE_END);
    }

    // MODIFIES: this, PasswordLogContainer
    // EFFECTS: updates the password log container if any changes were made and closes page
    private void closeLog() {
        if (updated) {
            container.updateLog(passwordLog);
        }
        PasswordApp.getPM().closeLog();
    }

    // EFFECTS: sets up the look of given text field field and adds it to the given panel parent
    private void setupField(JPanel parent, JTextField field) {
        field.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,0)));
        Dimension fieldSize = new Dimension(SCREEN_SIZE.width / 3, SCREEN_SIZE.height / 15);
        field.setPreferredSize(fieldSize);
        field.setForeground(Color.BLACK);
        field.setBackground(new Color(245, 245, 245));
        field.setFont(new Font("", Font.PLAIN, SCREEN_SIZE.width / 40));
        parent.add(field);
    }

    // EFFECTS: sets up the look of given text label label and adds it to the given panel parent
    private void setupLabel(JPanel parent, JLabel label) {
        parent.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.setBackground(Color.WHITE);
        label.setBackground(Color.WHITE);
        label.setFont(new Font("", Font.ITALIC, SCREEN_SIZE.width / 40));
        parent.add(label);
    }

    // EFFECTS: returns a label with an icon based on the strength rating of password in password log
    private JLabel makeStrengthIcon() {
        JLabel label = new JLabel();
        ImageIcon image;
        if (passwordLog.getPassword().getPasswordStrengthRating().equals("Strong")) {
            image = new ImageIcon("data/strong.png");
        } else if (passwordLog.getPassword().getPasswordStrengthRating().equals("Medium")) {
            image = new ImageIcon("data/medium.png");
        } else {
            image = new ImageIcon("data/poor.png");
        }
        label.setIcon(image);
        return label;
    }

    // MODIFIES: this, PasswordManager, PasswordLog
    // EFFECTS: saves any changes of the password log information to JSON if possible
    private void saveChanges() {
        savePassword();

        if (!userField.getText().equals(passwordLog.getUsername())) {
            PasswordApp.getPM().savePasswordManager("update", passwordLog, "username", userField.getText());
            passwordLog.setUsername(userField.getText());
            updated = true;
        }
        if (!urlField.getText().equals(passwordLog.getURL())) {
            PasswordApp.getPM().savePasswordManager("update", passwordLog, "url", urlField.getText());
            passwordLog.setURL(urlField.getText());
            updated = true;
        }
        if (!notesArea.getText().equals(passwordLog.getNotes())) {
            PasswordApp.getPM().savePasswordManager("update", passwordLog, "notes", notesArea.getText());
            passwordLog.setNotes(notesArea.getText());
            updated = true;
        }

        saveTitle();

        if (updated) {
            showSavedDialogue();
        }
    }

    // MODIFIES: this, PasswordManager, PasswordLog
    // EFFECTS: saves any changes of the password log password to JSON if possible
    private void savePassword() {
        if (!passwordField.getText().equals(passwordLog.getPassword().getPassword())) {
            if (passwordField.getText().length() == 0) {
                showErrorDialogue("Password");
                passwordField.setText(passwordLog.getPassword().getPassword());
                return;
            }
            PasswordApp.getPM().savePasswordManager("update", passwordLog, "password", passwordField.getText());
            passwordLog.updatePassword(new Password(passwordField.getText()));
            title.remove(1);
            title.add(makeStrengthIcon());
            repaint();
            revalidate();
            updated = true;
        }
    }

    // MODIFIES: this, PasswordManager, PasswordLog
    // EFFECTS: saves any changes of the password log title to JSON if possible
    private void saveTitle() {
        if (!titleField.getText().equals(passwordLog.getTitle())) {
            updated = false;
            if (titleField.getText().length() == 0) {
                showErrorDialogue("Title");
                titleField.setText(passwordLog.getTitle());
                return;
            }
            if (PasswordApp.getPM().getPm().viewPasswords().contains(titleField.getText())) {
                updated = false;
                JOptionPane.showConfirmDialog(null,
                        "A password with that title already exists. Please use another name.",
                        "Warning!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                titleField.setText(passwordLog.getTitle());
                return;
            }
            PasswordApp.getPM().savePasswordManager("update", passwordLog, "title", titleField.getText());
            passwordLog.updateTitle(titleField.getText());
            updated = true;
        }
    }

    // EFFECTS: shows save success message
    private void showSavedDialogue() {
        JOptionPane.showConfirmDialog(null,
                "Changes have been saved!",
                "Success!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: shows save error message related to error from given field field
    private void showErrorDialogue(String field) {
        String errorMsg = field + " cannot be empty!";
        JOptionPane.showConfirmDialog(null,
                errorMsg,
                "Error!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE);
    }
}
