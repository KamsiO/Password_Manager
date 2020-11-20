package ui.gui;

import model.Password;
import model.PasswordLog;

import javax.swing.*;
import java.awt.*;

public class PasswordLogPage extends JPanel {
    private PasswordLog passwordLog;
    private PasswordLogContainer container;

    private JPanel title;
    private JTextField titleField;
    private JTextField userField;
    private JTextField urlField;
    private JTextField passwordField;
    private JTextArea notesArea;

    private boolean updated = false;

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public PasswordLogPage(PasswordLogContainer container) {
        this.container = container;
        passwordLog = container.getLog();

        initializeGraphics();
    }

    private void initializeGraphics() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 0, 100));
        Component vgap = Box.createRigidArea(new Dimension(0,7));

        //infoPanel.add(vgap);
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
        //infoPanel.add(vgap);

        JPanel password = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel passwordLabel = new JLabel("Password: ");
        setupLabel(password, passwordLabel);
        passwordField = new JTextField(passwordLog.getPassword().getPassword());
        setupField(password, passwordField);
        infoPanel.add(password);
        //infoPanel.add(vgap);

        JPanel user = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel userLabel = new JLabel("Username: ");
        setupLabel(user, userLabel);
        userField = new JTextField(passwordLog.getUsername());
        setupField(user, userField);
        infoPanel.add(user);
        //infoPanel.add(vgap);

        JPanel url = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel urlLabel = new JLabel("URL: ");
        setupLabel(url, urlLabel);
        urlField = new JTextField(passwordLog.getURL());
        setupField(url, urlField);
        urlField.setForeground(new Color(33, 158, 204));
        infoPanel.add(url);
        //infoPanel.add(vgap);

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

        add(infoPanel);

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

    private void closeLog() {
        if (updated) {
            container.updateLog(passwordLog);
        }
        PasswordApp.getPM().closeLog();
    }

    private void setupField(JPanel parent, JTextField field) {
        field.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,0)));
        Dimension fieldSize = new Dimension(SCREEN_SIZE.width / 3, SCREEN_SIZE.height / 15);
        field.setPreferredSize(fieldSize);
        field.setForeground(Color.BLACK);
        field.setBackground(new Color(245, 245, 245));
        field.setFont(new Font("", Font.PLAIN, SCREEN_SIZE.width / 40));
        parent.add(field);
    }

    private void setupLabel(JPanel parent, JLabel label) {
        parent.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.setBackground(Color.WHITE);
        label.setBackground(Color.WHITE);
        label.setFont(new Font("", Font.ITALIC, SCREEN_SIZE.width / 40));
        parent.add(label);
    }

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

    public void saveChanges() {
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
        if (!titleField.getText().equals(passwordLog.getTitle())) {
            if (titleField.getText().length() == 0) {
                showErrorDialogue("Title");
                titleField.setText(passwordLog.getTitle());
                return;
            }
            PasswordApp.getPM().savePasswordManager("update", passwordLog, "title", titleField.getText());
            passwordLog.updateTitle(titleField.getText());
            updated = true;
        }
        //need to call method to change the strength (text or icon)
        showSavedDialogue();
    }

    private void showSavedDialogue() {
        JOptionPane.showConfirmDialog(null,
                "Changes have been saved!",
                "Success!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorDialogue(String field) {
        String errorMsg = field + " cannot be empty!";
        JOptionPane.showConfirmDialog(null,
                errorMsg,
                "Error!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE);
    }
}
