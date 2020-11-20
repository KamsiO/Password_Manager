package ui.gui;

import model.PasswordLog;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PasswordLogContainer extends JPanel {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private PasswordLog log;
    private String name;
    private String password;

    private JLabel title;
    private JLabel passwordLabel;

    public PasswordLogContainer(PasswordLog pl) {
        log = pl;
        name = pl.getTitle();
        password = pl.getPassword().getPassword();

        initializeGraphics();
    }

    private void initializeGraphics() {
        //setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Dimension min = new Dimension(SCREEN_SIZE.width / 15, SCREEN_SIZE.width / 12);
        Dimension max = new Dimension(min.width, min.height + 10);
        setMinimumSize(min);
        setMaximumSize(max);


        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        deletePanel.setBackground(Color.WHITE);
        ImageIcon deleteIcon = new ImageIcon("data/x.png");
        JButton delete = new JButton();
        delete.setIcon(deleteIcon);
        delete.setBackground(Color.WHITE);
        delete.setFocusable(false);
        delete.addActionListener(deleteLog);
        deletePanel.add(delete);
        //c.anchor = GridBagConstraints.EAST;
        //c.gridx = 1;
        //delete.setAlignmentX(Component.RIGHT_ALIGNMENT);
        add(deletePanel);

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.setBackground(Color.WHITE);
        title = new JLabel(name);
//        JTextArea title = new JTextArea(name, 3, 1);
//        title.setMinimumSize(new Dimension(min.width, min.height / 3));
//        title.setEditable(false);
//        title.setLineWrap(true);
//        title.setWrapStyleWord(true);
        if (name.length() > 13) {
            title.setFont(new Font("", Font.PLAIN, 64));
        } else {
            title.setFont(new Font("", Font.PLAIN, 90));
        }
        title.setForeground(new Color(97, 94, 110));
        c.gridy = 1;
        //c.gridy = 0;
        titlePanel.add(title);
        add(titlePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordLabel = new JLabel(password);
        passwordLabel.setFont(new Font("", Font.PLAIN, 60));
        //c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(70, 0, 0, 0);
        //c.anchor = GridBagConstraints.NONE;
        passwordPanel.add(passwordLabel);
        add(passwordPanel);

        JPanel copyBtnPanel = new JPanel(new FlowLayout());
        copyBtnPanel.setBackground(Color.WHITE);
        ImageIcon copyIcon = new ImageIcon("data/copy.png");
        JButton copy = new JButton();
        copy.setIcon(copyIcon);
        copy.setBackground(Color.WHITE);
        copy.setFocusable(false);
        copy.addActionListener(copyToClipboard);
        c.gridy = 3;
        copyBtnPanel.add(copy);
        add(copyBtnPanel);

        //https://stackoverflow.com/questions/22936774/how-do-i-simplify-mouselistener-so-that-i-dont-have-all-these-unused-methods
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                viewThisLog();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(new Color(43, 149, 207)));
                //setBackground(new Color(75, 193, 222));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });
    }

    private void viewThisLog() {
        System.out.println("click");
        PasswordApp.getPM().viewLog(this);
    }

    public PasswordLog getLog() {
        return log;
    }

    public void updateLog(PasswordLog log) {
        this.log = log;
        title.setText(log.getTitle());
        passwordLabel.setText(log.getPassword().getPassword());
        repaint();
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
