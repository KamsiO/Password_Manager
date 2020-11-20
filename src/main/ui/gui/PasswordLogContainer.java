package ui.gui;

import model.PasswordLog;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A JPanel that represents a password log with a title and password
 */

public class PasswordLogContainer extends JPanel {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private PasswordLog log;
    private String name;
    private String password;

    private JLabel title;
    private JLabel passwordLabel;

    // EFFECTS: creates a password log container representing the give password log pl
    public PasswordLogContainer(PasswordLog pl) {
        log = pl;
        name = pl.getTitle();
        password = pl.getPassword().getPassword();

        initializeGraphics();
        createMouseListener();
    }

    // MODIFIES: this
    // EFFECTS: updates the title and password to the title and password of given password log log
    public void updateLog(PasswordLog log) {
        this.log = log;
        title.setText(log.getTitle());
        passwordLabel.setText(log.getPassword().getPassword());
        repaint();
    }

    public PasswordLog getLog() {
        return log;
    }

    // MODIFIES: this
    // EFFECTS: creates and adds all the components to this
    private void initializeGraphics() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Dimension min = new Dimension(SCREEN_SIZE.width / 15, SCREEN_SIZE.width / 12);
        Dimension max = new Dimension(min.width, min.height + 10);
        setMinimumSize(min);
        setMaximumSize(max);

        addDeleteButton();
        addTitle();
        addPassword();
        addCopyButton();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a delete button to this
    private void addDeleteButton() {
        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        deletePanel.setBackground(Color.WHITE);
        ImageIcon deleteIcon = new ImageIcon("data/x.png");
        JButton delete = new JButton();
        delete.setIcon(deleteIcon);
        delete.setBackground(Color.WHITE);
        delete.setFocusable(false);
        delete.addActionListener(evt -> checkDelete());
        deletePanel.add(delete);
        add(deletePanel);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a title label with the password log title to this
    private void addTitle() {
        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.setBackground(Color.WHITE);
        title = new JLabel(name);
        if (name.length() > 13) {
            title.setFont(new Font("", Font.PLAIN, 64));
        } else {
            title.setFont(new Font("", Font.PLAIN, 90));
        }
        title.setForeground(new Color(97, 94, 110));
        titlePanel.add(title);
        add(titlePanel);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a password label with the password log password to this
    private void addPassword() {
        JPanel passwordPanel = new JPanel(new FlowLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordLabel = new JLabel(password);
        passwordLabel.setFont(new Font("", Font.PLAIN, 60));
        passwordPanel.add(passwordLabel);
        add(passwordPanel);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a copy to clipboard button to this
    private void addCopyButton() {
        JPanel copyBtnPanel = new JPanel(new FlowLayout());
        copyBtnPanel.setBackground(Color.WHITE);
        ImageIcon copyIcon = new ImageIcon("data/copy.png");
        JButton copy = new JButton();
        copy.setIcon(copyIcon);
        copy.setBackground(Color.WHITE);
        copy.setFocusable(false);
        copy.addActionListener(evt -> copyToClipboard());
        copyBtnPanel.add(copy);
        add(copyBtnPanel);
    }

    // MODIFIES: this
    // EFFECTS: adds mouse handling features to this
    private void createMouseListener() {
        //https://stackoverflow.com/questions/22936774/how-do-i-simplify-mouselistener-so-that-i-dont-have-all-these-unused-methods
        addMouseListener(new MouseAdapter() {
            @Override
            // MODIFIES: PasswordManagerPage
            // EFFECTS: opens a password log page based on password log from this
            public void mouseClicked(MouseEvent e) {
                viewThisLog();
            }

            // MODIFIES: this
            // EFFECTS: highlights the border of this when user's mouse enters this
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(new Color(43, 149, 207)));
            }

            // MODIFIES: this
            // EFFECTS: sets the border of this back to normal when user's mouse exits this
            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });
    }

    // MODIFIES: PasswordManagerPage
    // EFFECTS: creates a password log page based on the password log this represents
    private void viewThisLog() {
        System.out.println("click");
        PasswordApp.getPM().viewLog(this);
    }

    // EFFECTS: copies the password to clipboard
    //https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
    private void copyToClipboard() {
        StringSelection stringSelection = new StringSelection(password);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    // MODIFIES: PasswordManagerPage, PasswordManager
    // EFFECTS: confirms if user wants to delete a password
    //          deletes if yes, do nothing otherwise
    private void checkDelete() {
        int delete = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this password?", "Delete?", JOptionPane.YES_NO_OPTION);
        if (delete == 0) {
            deleteLog();
        }
    }

    // MODIFIES: PasswordManagerPage, PasswordManager
    private void deleteLog() {
        PasswordApp.getPM().deleteLog(this);
    }

}
