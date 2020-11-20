package ui.gui;

import model.Password;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

/**
 * Responsible for the password generation section of the gui
 */

public class GeneratePasswordPage extends JPanel {
    private JPanel page1;
    private JPanel page2;

    private JLabel startLabel;
    private JLabel passwordLabel;
    private String password;

    private JButton generateBtn;
    private JButton copyBtn;
    private PasswordSaver saver;

    private int colorChange;
    private int alphaChange;

    private Timer timer;

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public GeneratePasswordPage() {
        saver = new PasswordSaver();
        initializeGraphics();
    }

    //EFFECTS: adds the start page of the password generation section to the JPanel
    public void initializeGraphics() {
        setLayout(new CardLayout(0, 0));
        add(makeStartPage(), "start");
    }

    //MODIFIES: this
    //EFFECTS: creates the start page and adds associated components to the page
    public JPanel makeStartPage() {
        page1 = new JPanel();
        page1.setBackground(new Color(48,48,48));

        page1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        startLabel = makeStartPageLabel();
        c.insets = new Insets(-(SCREEN_SIZE.width / 20), 0, 0, 0);
        c.gridwidth = 3;
        page1.add(startLabel, c);

        makeGenerateBtn();
        c.ipadx = SCREEN_SIZE.width / 6;
        c.ipady = SCREEN_SIZE.width / 50;
        c.gridwidth = 3;
        c.gridy = 1;
        c.insets = new Insets(SCREEN_SIZE.width / 20, 0, 0, 0);
        page1.add(generateBtn, c);

        return page1;
    }

    //MODIFIES: this
    //EFFECTS: creates the page that shows the password and adds associated components to the page
    public JPanel makeGeneratedPage() {
        page2 = new JPanel();
        page2.setBackground(Color.BLACK);

        page2.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel msg = new JLabel("Your new password is:");
        msg.setForeground(Color.WHITE);
        msg.setFont(new Font("", Font.PLAIN, SCREEN_SIZE.width / 54));

        passwordLabel = new JLabel(password);
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("", Font.BOLD, SCREEN_SIZE.width / 17));

        c.insets = new Insets(-(SCREEN_SIZE.height / 10), 0, 0, 0);
        c.gridy = 0;
        c.gridwidth = 3;
        page2.add(msg, c);

        c.gridy = 1;
        c.insets = new Insets(-50, 0, 0, 0);
        page2.add(passwordLabel, c);

        placeButtons();

        return page2;
    }

    // MODIFIES: this
    // EFFECTS: generates a strong password and shows the password on a new page
    public void generate() {
        Password pw = new Password();
        pw.generateStrongPassword();
        password = pw.getPassword();

        saver.setPassword(password);

        doTransition();
    }

    // MODIFIES: this
    // EFFECTS: does a fade transition between the start page and the page that shows the password
    private void doTransition() {
        colorChange = 1;
        alphaChange = 55;

        ActionListener taskPerformer = evt -> {
            if (colorChange > 48) {
                timer.stop();
                changePage();
                return;
            }
            int color = 48 - colorChange;
            int alpha = 255 - alphaChange;
            page1.setBackground(new Color(color,color,color));
            startLabel.setForeground(new Color(255, 255, 255, alpha));
            generateBtn.setBackground(new Color(74, 74, 74, alpha));
            repaint();
            colorChange += 3;
            if (alphaChange < 255) {
                alphaChange += 20;
            }
        };

        timer = new Timer(40, taskPerformer);
        timer.start();
    }

    // MODIFIES: this
    // EFFECTS: copies the password to clipboard, and changes the text of the copy button
    ActionListener copyToClipboard = new ActionListener() {
        //https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
        public void actionPerformed(ActionEvent evt) {
            StringSelection stringSelection = new StringSelection(password);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            copyBtn.setText("Copied!");

            ActionListener changeCopyBtnText = evt1 -> {
                copyBtn.setText("Copy to Clipboard?");
                timer.stop();
            };

            timer = new Timer(2000, changeCopyBtnText);
            timer.start();
        }
    };

    // MODIFIES: this
    // EFFECTS: changes the password label to a new strong password
    ActionListener reGenerate = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            Password pw = new Password();
            pw.generateStrongPassword();
            password = pw.getPassword();
            saver.setPassword(password);

            ActionListener changeLabelText = evt1 -> {
                if (alphaChange > 255) {
                    timer.stop();
                    passwordLabel.setText(password);
                    passwordLabel.setForeground(Color.WHITE);
                    return;
                }
                int alpha = 255 - alphaChange;
                passwordLabel.setForeground(new Color(255, 255, 255, alpha));
                repaint();
                alphaChange += 20;
            };

            timer = new Timer(20, changeLabelText);
            alphaChange = 55;
            timer.start();
        }
    };

    // EFFECTS: switches from the start page to the page that shows the password
    private void changePage() {
        add(makeGeneratedPage(), "generated");
        CardLayout cl = (CardLayout)(getLayout());
        cl.show(this, "generated");
    }

    // EFFECTS: creates the label on the start page
    private JLabel makeStartPageLabel() {
        JLabel title = new JLabel("Password Generator");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("", Font.BOLD, SCREEN_SIZE.width / 20));

        return title;
    }

    // EFFECTS: creates the generate button on the start page and assigns it an action
    private void makeGenerateBtn() {
        generateBtn = new JButton("Generate");
        generateBtn.setFont(new Font("", Font.ITALIC, 40));
        generateBtn.setForeground(new Color(190, 190, 190));
        generateBtn.setBackground(new Color(74, 74, 74));
        generateBtn.setFocusable(false);
        generateBtn.addActionListener(evt -> generate());
    }

    // MODIFIES: this
    // EFFECTS: creates the buttons that present the user with options on the page that shows the password
    private List<JButton> makeOptionBtns() {
        JButton retry = new JButton("Re-Generate?");
        retry.setFont(new Font("", Font.ITALIC, 40));
        retry.setForeground(new Color(150, 150, 150));
        retry.setBackground(new Color(40, 40, 40));
        retry.setFocusable(false);
        retry.addActionListener(reGenerate);

        copyBtn = new JButton("Copy to Clipboard?");
        copyBtn.setFont(new Font("", Font.ITALIC, 40));
        copyBtn.setForeground(new Color(150, 150, 150));
        copyBtn.setBackground(new Color(40, 40, 40));
        copyBtn.setFocusable(false);
        copyBtn.addActionListener(copyToClipboard);

        JButton save = new JButton("Save Password?");
        save.setFont(new Font("", Font.ITALIC, 40));
        save.setForeground(new Color(150, 150, 150));
        save.setBackground(new Color(40, 40, 40));
        save.setFocusable(false);
        save.addActionListener(saver);

        List<JButton> buttons = new ArrayList<>();
        buttons.add(retry);
        buttons.add(copyBtn);
        buttons.add(save);

        return buttons;
    }

    // MODIFIES: this
    // EFFECTS: adds the option buttons to the page that shows the password
    private void placeButtons() {
        List<JButton> buttons = makeOptionBtns();
        GridBagConstraints constraintsForBtns = new GridBagConstraints();

        constraintsForBtns.insets = new Insets(SCREEN_SIZE.height / 15, 5, 0, 5);
        constraintsForBtns.gridx = 0;
        constraintsForBtns.gridy = 2;
        constraintsForBtns.ipady = SCREEN_SIZE.width / 70;
        constraintsForBtns.ipadx = SCREEN_SIZE.width / 15;
        constraintsForBtns.weightx = 0.8;
        page2.add(buttons.get(0), constraintsForBtns);

        constraintsForBtns.gridx = 1;
        page2.add(buttons.get(1), constraintsForBtns);

        constraintsForBtns.gridx = 2;
        page2.add(buttons.get(2), constraintsForBtns);
    }
}
