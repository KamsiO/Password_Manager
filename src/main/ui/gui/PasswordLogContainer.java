package ui.gui;

import model.PasswordLog;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordLogContainer extends JPanel {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private PasswordLog log;
    private String name;
    private String password;

    public PasswordLogContainer(PasswordLog pl) {
        log = pl;
        name = pl.getTitle();
        password = pl.getPassword().getPassword();

        initializeGraphics();
    }

    private void initializeGraphics() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Dimension min = new Dimension(SCREEN_SIZE.width / 15, SCREEN_SIZE.width / 12);
        setPreferredSize(min);

        JLabel title = new JLabel(name.toUpperCase());
//        JTextArea title = new JTextArea(name.toUpperCase());
//        title.setEditable(false);
//        title.setLineWrap(true);
//        title.setWrapStyleWord(true);
        title.setFont(new Font("", Font.PLAIN, 90));
        title.setForeground(new Color(97, 94, 110));
        c.gridx = 0;
        c.gridy = 0;
        add(title, c);

        ImageIcon deleteIcon = new ImageIcon("data/x.png");
        JButton delete = new JButton();
        delete.setIcon(deleteIcon);
        delete.setBackground(Color.WHITE);
        delete.setFocusable(false);
        delete.addActionListener(deleteLog);
        //c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        add(delete, c);

        JLabel passwordLabel = new JLabel(password);
        passwordLabel.setFont(new Font("", Font.PLAIN, 60));
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(70, 0, 0, 0);
        //c.anchor = GridBagConstraints.NONE;
        add(passwordLabel, c);

        ImageIcon copyIcon = new ImageIcon("data/copy.png");
        JButton copy = new JButton();
        copy.setIcon(copyIcon);
        copy.setBackground(Color.WHITE);
        copy.setFocusable(false);
        copy.addActionListener(copyToClipboard);
        c.gridx = 1;
        add(copy, c);
    }

    public PasswordLog getLog() {
        return log;
    }

    // EFFECTS: copies the password to clipboard
    ActionListener copyToClipboard = new ActionListener() {
        //https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
        public void actionPerformed(ActionEvent evt) {
            StringSelection stringSelection = new StringSelection(password);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    };

    ActionListener deleteLog = evt -> {
        int delete = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this password?", "Delete?", JOptionPane.YES_NO_OPTION);
        if (delete == 0) {
            deleteLog();
        }
    };

    private void deleteLog() {
        PasswordApp.getPM().deleteLog(this);
    }
}
