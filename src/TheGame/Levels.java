package TheGame;


import com.sun.opengl.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.media.opengl.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Levels extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new Levels();
    }

    String PLAYER_NAME;
    JButton EASY, NORMAL, HARD, BACK, MULTIPLAYER;

    public Levels() {
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
            PLAYER_NAME = JOptionPane.showInputDialog(this, "Enter Your Name");
            String MSG = "HI " + PLAYER_NAME + " , Are You Ready?";
            if (PLAYER_NAME != null && PLAYER_NAME.length() != 0) {
                int RET = JOptionPane.showConfirmDialog(this, MSG, "Ready MSG?", JOptionPane.YES_NO_OPTION);
                if (RET == JOptionPane.YES_OPTION) {
                    // Game Code.
                    System.out.println("EASY");
                }

            }
        }
        if (e.getSource().equals(NORMAL)) {
            PLAYER_NAME = JOptionPane.showInputDialog(this, "Enter Your Name");
            String MSG = "HI " + PLAYER_NAME + " , Are You Ready?";
            if (PLAYER_NAME != null && PLAYER_NAME.length() != 0) {
                int RET = JOptionPane.showConfirmDialog(this, MSG, "Ready MSG?", JOptionPane.YES_NO_OPTION);
                if (RET == JOptionPane.YES_OPTION) {
                    new Normal();
                    setVisible(false);
                }

            }
        }
        if (e.getSource().equals(HARD)) {
            PLAYER_NAME = JOptionPane.showInputDialog(this, "Enter Your Name");
            String MSG = "HI " + PLAYER_NAME + " , Are You Ready?";
            if (PLAYER_NAME != null && PLAYER_NAME.length() != 0) {
                int RET = JOptionPane.showConfirmDialog(this, MSG, "Ready MSG?", JOptionPane.YES_NO_OPTION);
                if (RET == JOptionPane.YES_OPTION) {
                    // Game Code.
                    System.out.println("HARD");
                }

            }
        }
        if (e.getSource().equals(BACK)) {
            new RiverRaid();
            setVisible(false);
        }


    }
}