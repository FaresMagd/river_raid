package TheGame;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class LevelsScreen extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new LevelsScreen();
    }

    String FirstPlayerName;
    String SecondPlayerName;
    JButton EASY, NORMAL, HARD, BACK, MULTIPLAYER;

    public LevelsScreen() {
        // The Buttons:
        EASY = new JButton("EASY");
        NORMAL = new JButton("NORMAL");
        HARD = new JButton("HARD");
        BACK = new JButton("BACK");
        MULTIPLAYER = new JButton("Multiplayer");

        // The Buttons Background:
        EASY.setBackground(Color.BLUE);
        NORMAL.setBackground(Color.BLUE);
        HARD.setBackground(Color.BLUE);
        BACK.setBackground(Color.BLUE);
        MULTIPLAYER.setBackground(Color.BLUE);

        // The Buttons Text Color:
        EASY.setForeground(Color.WHITE);
        NORMAL.setForeground(Color.WHITE);
        HARD.setForeground(Color.WHITE);
        BACK.setForeground(Color.WHITE);
        MULTIPLAYER.setForeground(Color.WHITE);

        // The Buttons Position & Size:
        EASY.setBounds(250, 150, 200, 100);
        NORMAL.setBounds(250, 275, 200, 100);
        HARD.setBounds(250, 400, 200, 100);
        BACK.setBounds(25, 25, 75, 50);
        MULTIPLAYER.setBounds(250, 525, 200, 100);

        // The Buttons Border:
        EASY.setBorder(new LineBorder(Color.WHITE, 1, true));
        NORMAL.setBorder(new LineBorder(Color.WHITE, 1, true));
        HARD.setBorder(new LineBorder(Color.WHITE, 1, true));
        BACK.setBorder(new LineBorder(Color.WHITE, 1, true));
        MULTIPLAYER.setBorder(new LineBorder(Color.WHITE, 1, true));


        Font F = new Font("Arial", Font.PLAIN, 20);
        EASY.setFont(F);
        NORMAL.setFont(F);
        HARD.setFont(F);
        MULTIPLAYER.setFont(F);
        Font F1 = new Font("Arial", Font.PLAIN, 15);
        BACK.setFont(F1);


        setLayout(null);
        ImageIcon backgroundIcon = new ImageIcon("src/Assets//Bground.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
        add(backgroundLabel);

        backgroundLabel.add(EASY);
        backgroundLabel.add(NORMAL);
        backgroundLabel.add(HARD);
        backgroundLabel.add(BACK);
        backgroundLabel.add(MULTIPLAYER);

        EASY.addActionListener(this);
        EASY.setFocusable(false);
        NORMAL.addActionListener(this);
        NORMAL.setFocusable(false);
        HARD.addActionListener(this);
        HARD.setFocusable(false);
        BACK.addActionListener(this);
        BACK.setFocusable(false);
        MULTIPLAYER.addActionListener(this);
        MULTIPLAYER.setFocusable(false);


        setTitle("River Raid Levels");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(EASY)) {
            GetGameFromDifficulty(0.90);
        }
        if (e.getSource().equals(NORMAL)) {
            GetGameFromDifficulty(1);
        }
        if (e.getSource().equals(HARD)) {
            GetGameFromDifficulty(1.2);
        }
        if (e.getSource().equals(BACK)) {
            new RiverRaid();
            setVisible(false);
        }
        if (e.getSource().equals(MULTIPLAYER)) {
            GetMultiplayerGame();
        }


    }

    public void GetGameFromDifficulty(double speedFactor){
        FirstPlayerName = JOptionPane.showInputDialog(this, "Enter Your Name");
        String MSG = "HI " + FirstPlayerName + " , Are You Ready?";
        if (FirstPlayerName != null && !FirstPlayerName.isEmpty()) {
            int RET = JOptionPane.showConfirmDialog(this, MSG, "Ready MSG?", JOptionPane.YES_NO_OPTION);
            if (RET == JOptionPane.YES_OPTION) {
                new Game(FirstPlayerName, speedFactor);
                setVisible(false);
            }

        }
    }

    public void GetMultiplayerGame (){
        FirstPlayerName = JOptionPane.showInputDialog(this, "Enter First Player Name");
        SecondPlayerName = JOptionPane.showInputDialog(this, "Enter Second Player Name");

        String MSG = "HI " + FirstPlayerName + " , Are You Ready?";
        if (FirstPlayerName != null && !FirstPlayerName.isEmpty() && SecondPlayerName != null && !SecondPlayerName.isEmpty() ) {
            int RET = JOptionPane.showConfirmDialog(this, MSG, "Ready MSG?", JOptionPane.YES_NO_OPTION);
            if (RET == JOptionPane.YES_OPTION) {
                new Game(FirstPlayerName, SecondPlayerName);
                setVisible(false);
            }

        }
    }

}