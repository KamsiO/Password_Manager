package ui.gui;

import model.Password;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;

/**
 * Responsible for the password strength checker section of the gui
 */

public class CheckStrengthPage extends JPanel implements ActionListener, KeyListener {
    private JPanel page1;
    private JPanel page2;

    private Password password;
    private PasswordSaver saver;

    private JLabel startLabel;

    private JButton checkBtn;
    private JTextField textField;

    private int colorChange;
    private int alphaChange;

    private Timer timer;

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public CheckStrengthPage() {
        saver = new PasswordSaver();
        initializeGraphics();
    }


    public void initializeGraphics() {
        setLayout(new CardLayout(0, 0));
        add(makeStartPage(), "start");
    }

    //MODIFIES: this
    //EFFECTS: creates the start page and adds associated components to the page
    public JPanel makeStartPage() {
        page1 = new JPanel();
        page1.setBackground(new Color(135, 135, 135));

        page1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        startLabel = makeStartPageLabel();
        c.insets = new Insets(-(SCREEN_SIZE.width / 20), 0, 0, 0);
        c.gridwidth = 3;
        page1.add(startLabel, c);

        makeTextField();
        c.ipady = 20;
        c.gridy = 1;
        c.insets = new Insets(SCREEN_SIZE.width / 20, 0, 0, 0);
        page1.add(textField, c);

        makeCheckBtn();
        c.gridy = 2;
        c.ipadx = SCREEN_SIZE.width / 10;
        c.ipady = SCREEN_SIZE.width / 50;
        c.insets = new Insets(SCREEN_SIZE.height / 30, 0, 0, 0);
        page1.add(checkBtn, c);

        return page1;
    }

    //MODIFIES: this
    //EFFECTS: creates the page that shows the password and adds associated components to the page
    public JPanel makeStrengthPage() {
        page2 = new JPanel();
        page2.setBackground(new Color(235, 235, 235));

        page2.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel msg = new JLabel("The strength of " + password.getPassword() + " is:");
        msg.setForeground(new Color(97, 94, 110));
        msg.setFont(new Font("", Font.PLAIN, SCREEN_SIZE.width / 44));

        JLabel strength = new JLabel(password.getPasswordStrengthRating().toUpperCase());
        strength.setForeground(setStrengthColor());
        strength.setFont(new Font("", Font.BOLD, SCREEN_SIZE.width / 44));

        c.insets = new Insets(0, 0, 20, 20);
        //c.gridx = 0;
        c.gridy = 0;
        page2.add(msg, c);

        c.insets = new Insets(0, msg.getWidth() + 10, 20, 0);
        //c.gridx = 0;
        c.gridy = 0;
        page2.add(strength, c);

        placeRequirements();
        placeButtons();

        return page2;
    }


    // MODIFIES: this
    // EFFECTS:
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkBtn) {
            password = new Password(textField.getText());

            saver.setPassword(password.getPassword());

            doTransitionToStrength();
        }
    }

    // MODIFIES: this
    // EFFECTS: does a fade transition between the start page and the page that shows the strength
    private void doTransitionToStart() {
        colorChange = 1;

        ActionListener taskPerformer = evt -> {
            int color = 235 - colorChange;
            if (color < 135) {
                timer.stop();
                changePageToStart();
                return;
            }

            page2.setBackground(new Color(color,color,color));
            repaint();

            colorChange += 3;

        };
        timer = new Timer(20, taskPerformer);
        timer.start();
    }

    // MODIFIES: this
    // EFFECTS: does a fade transition between the start page and the page that shows the strength
    private void doTransitionToStrength() {
        colorChange = 1;
        alphaChange = 55;

        ActionListener taskPerformer = evt -> {
            int color = 135 + colorChange;
            int alpha = 255 - alphaChange;
            if (color > 235) {
                timer.stop();
                changePageToStrength();
                return;
            }

            page1.setBackground(new Color(color,color,color));
            startLabel.setForeground(new Color(235, 235, 235, alpha));
            checkBtn.setBackground(new Color(190, 190, 190, alpha));
            textField.setBackground(new Color(235, 235, 235, alpha));
            repaint();
            colorChange += 3;
            if (alphaChange < 255) {
                alphaChange += 20;
            }
        };
        timer = new Timer(20, taskPerformer);
        timer.start();
    }


    // EFFECTS:
    private void changePageToStrength() {
        add(makeStrengthPage(), "strength");
        CardLayout cl = (CardLayout)(getLayout());
        cl.show(this, "strength");
    }

    private void changePageToStart() {
        add(makeStartPage(), "start");
        CardLayout cl = (CardLayout)(getLayout());
        cl.show(this, "start");
    }

    // EFFECTS: creates the label on the start page
    private JLabel makeStartPageLabel() {
        JLabel title = new JLabel("Password Strength Checker");
        title.setForeground(new Color(235, 235, 235));
        title.setFont(new Font("", Font.BOLD, SCREEN_SIZE.width / 25));

        return title;
    }

    // EFFECTS: creates the check button on the start page and assigns it an action
    private void makeCheckBtn() {
        checkBtn = new JButton("Check!");
        checkBtn.setFont(new Font("", Font.ITALIC, 40));
        checkBtn.setBackground(new Color(175, 175, 175));
        checkBtn.setForeground(Color.WHITE);
        checkBtn.setFocusable(false);
        checkBtn.setEnabled(false);
        checkBtn.addActionListener(this);
    }

    // EFFECTS: creates the text field for the password on the start page and assigns it a key listener
    private void makeTextField() {
        textField = new JTextField();
        textField.setFont(new Font("", Font.PLAIN, 50));
        textField.setHorizontalAlignment(JTextField.CENTER);
        Dimension fieldSize = new Dimension(SCREEN_SIZE.width / 3, SCREEN_SIZE.height / 27);
        textField.setPreferredSize(fieldSize);

        textField.addKeyListener(this);
    }

    // MODIFIES: this
    // EFFECTS: adds the option buttons to the page that shows the strength
    private void placeButtons() {
        List<JButton> buttons = makeOptionBtns();
        GridBagConstraints constraintsForBtns = new GridBagConstraints();

        constraintsForBtns.insets = new Insets(SCREEN_SIZE.height / 15, -SCREEN_SIZE.width / 9, 0, 5);
        //constraintsForBtns.gridx = 1;
        constraintsForBtns.gridy = 6;
        constraintsForBtns.ipady = SCREEN_SIZE.width / 70;
        constraintsForBtns.ipadx = SCREEN_SIZE.width / 25;
        //constraintsForBtns.weightx = 0.8;
        page2.add(buttons.get(0), constraintsForBtns);

        //constraintsForBtns.gridx = 3;
        constraintsForBtns.insets = new Insets(SCREEN_SIZE.height / 15, -SCREEN_SIZE.width / 11, 0, 5);
        page2.add(buttons.get(1), constraintsForBtns);
    }

    // MODIFIES: this
    // EFFECTS: creates the buttons that present the user with options on the page that shows the strength
    private List<JButton> makeOptionBtns() {
        JButton checkAgain = new JButton("Check Another?");
        checkAgain.setFont(new Font("", Font.ITALIC, 40));
        checkAgain.setForeground(new Color(97, 94, 110));
        checkAgain.setBackground(new Color(215, 215, 215));
        checkAgain.setFocusable(false);
        checkAgain.addActionListener(checkStrengthAgain);

        JButton save = new JButton("Save Password?");
        save.setFont(new Font("", Font.ITALIC, 40));
        save.setForeground(new Color(97, 94, 110));
        save.setBackground(new Color(215, 215, 215));
        save.setFocusable(false);
        save.addActionListener(saver);

        List<JButton> buttons = new ArrayList<>();
        buttons.add(checkAgain);
        buttons.add(save);

        return buttons;
    }

    private void placeRequirements() {
        List<JLabel> labels = makeRequirements();
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(25, 0, 0, 0);
        //c.gridx = 0;
        c.gridy = 1;
        page2.add(labels.get(0), c);

        c.insets = new Insets(15, 0, 0, 0);
        c.gridy = 2;
        page2.add(labels.get(1), c);

        c.gridy = 3;
        page2.add(labels.get(2), c);

        c.gridy = 4;
        page2.add(labels.get(3), c);

        c.gridy = 5;
        page2.add(labels.get(4), c);
    }

    private List<JLabel> makeRequirements() {
        String[] reqNames = {"Length is 8 or more characters:",
                "Has at least one lowercase character:",
                "Has at least one uppercase character:",
                "Has at least one digit:",
                "Has at least one special character:"};

        List<JLabel> labels = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            JLabel label = new JLabel(reqNames[i]);
            label.setForeground(new Color(97, 94, 110));
            label.setFont(new Font("", Font.PLAIN, SCREEN_SIZE.width / 60));
            label.setIcon(meetsRequirement(i));
            label.setHorizontalTextPosition(JLabel.LEFT);
            labels.add(label);
        }

        return labels;
    }

    private ImageIcon meetsRequirement(int index) {
        ImageIcon image;
        if (password.getPasswordStrength().get(index)) {
            image = new ImageIcon("data/check.png");
        } else {
            image = new ImageIcon("data/x.png");
        }
        return image;
    }

    private Color setStrengthColor() {
        if (password.getPasswordStrengthRating().equals("Strong")) {
            return new Color(56, 212, 51);
        } else if (password.getPasswordStrengthRating().equals("Medium")) {
            return new Color(255, 170, 0);
        } else {
            return new Color(245, 17, 17);
        }
    }

    ActionListener checkStrengthAgain = evt -> doTransitionToStart();

    @Override
    public void keyTyped(KeyEvent e) {
        //do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //do nothing
    }

    // MODIFIES: this
    // EFFECTS: enables the check button when user starts typing in the text field
    @Override
    public void keyReleased(KeyEvent e) {
        checkBtn.setEnabled(textField.getText().length() > 0);
    }
}

