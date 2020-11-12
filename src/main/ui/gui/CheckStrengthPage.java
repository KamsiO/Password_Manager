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

    private JLabel startLabel;
    private JLabel passwordLabel;
    private String password;

    private JButton checkBtn;
    private JTextField textField;
    private JButton copyBtn;

    private int colorChange;
    private int alphaChange;

    private Timer timer;

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public CheckStrengthPage() {
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


    // MODIFIES: this
    // EFFECTS: generates a strong password and shows the password on a new page
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkBtn) {
            Password pw = new Password();
            pw.generateStrongPassword();
            password = pw.getPassword();

            doTransition();
        }
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
            checkBtn.setBackground(new Color(74, 74, 74, alpha));
            repaint();
            colorChange += 3;
            if (alphaChange < 255) {
                alphaChange += 20;
            }
        };
        timer = new Timer(70, taskPerformer);
        timer.start();
    }


    // EFFECTS: switches from the start page to the page that shows the password
    private void changePage() {
        //add(makeGeneratedPage(), "generated");
        CardLayout cl = (CardLayout)(getLayout());
        cl.show(this, "strength");
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
        checkBtn.setBackground(new Color(190, 190, 190));
        checkBtn.setForeground(Color.WHITE);
        checkBtn.setFocusable(false);
        checkBtn.setEnabled(false);
        //checkBtn.addActionListener(this);
    }

    // EFFECTS: creates the text field for the password on the start page and assigns it a key listener
    private void makeTextField() {
        textField = new JTextField();
        textField.setFont(new Font("", Font.PLAIN, 50));
        textField.setHorizontalAlignment(JTextField.CENTER);
        Dimension labelSize = new Dimension(SCREEN_SIZE.width / 3, SCREEN_SIZE.height / 27);
        textField.setPreferredSize(labelSize);
        //textField.setSize(SCREEN_SIZE.width, 20);


        textField.addKeyListener(this);
    }

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
        if (textField.getText().length() > 0) {
            checkBtn.setEnabled(true);
        } else {
            checkBtn.setEnabled(false);
        }
    }
}

