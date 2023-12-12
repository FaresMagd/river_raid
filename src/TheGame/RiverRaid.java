package TheGame;

import com.sun.opengl.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.media.opengl.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class RiverRaid extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new RiverRaid();
    }

    JButton START, CREATORS, HOW_TO_PLAY, HIGH_SCORE, EXIT;
    AudioInputStream AUDIO_STREAM;
    Clip CLIP;

    public RiverRaid() {

        GLCanvas glcanvas;
        Animator animator;
        animator = new FPSAnimator(25);
        animator.start();


        try {
//            AUDIO_STREAM = AudioSystem.getAudioInputStream(new File("src/Assets/commercial-aircraft-in-flight-announcement-5-17499.wav"));
//            CLIP = AudioSystem.getClip();
//            CLIP.open(AUDIO_STREAM);
//            CLIP.loop(7);
//            CLIP.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }


        // The Buttons:
        START = new JButton("START");
        CREATORS = new JButton("CREATORS");
        HOW_TO_PLAY = new JButton("INSTRUCTIONS");
        HIGH_SCORE = new JButton("HIGH SCORE");
        EXIT = new JButton("EXIT");

        Font F = new Font("Arial", Font.PLAIN, 20);
        START.setFont(F);
        CREATORS.setFont(F);
        HOW_TO_PLAY.setFont(F);
        HIGH_SCORE.setFont(F);
        EXIT.setFont(F);

        // The Buttons Background:
        START.setBackground(Color.BLUE);
        CREATORS.setBackground(Color.BLUE);
        HOW_TO_PLAY.setBackground(Color.BLUE);
        HIGH_SCORE.setBackground(Color.BLUE);
        EXIT.setBackground(Color.BLUE);


        // The Buttons Text Color:
        START.setForeground(Color.WHITE);
        CREATORS.setForeground(Color.WHITE);
        HOW_TO_PLAY.setForeground(Color.WHITE);
        HIGH_SCORE.setForeground(Color.WHITE);
        EXIT.setForeground(Color.WHITE);


        // The Buttons Position & Size:
        START.setBounds(250, 150, 200, 85);
        CREATORS.setBounds(250, 250, 200, 85);
        HOW_TO_PLAY.setBounds(250, 350, 200, 85);
        HIGH_SCORE.setBounds(250, 450, 200, 85);
        EXIT.setBounds(250, 550, 200, 85);

        // The Buttons Border:
        START.setBorder(new LineBorder(Color.WHITE, 1, true));
        CREATORS.setBorder(new LineBorder(Color.WHITE, 1, true));
        HOW_TO_PLAY.setBorder(new LineBorder(Color.WHITE, 1, true));
        HIGH_SCORE.setBorder(new LineBorder(Color.WHITE, 1, true));
        EXIT.setBorder(new LineBorder(Color.WHITE, 1, true));


        setLayout(null);
        ImageIcon backgroundIcon = new ImageIcon("src/Assets//Bground.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
        add(backgroundLabel);

        backgroundLabel.add(START);
        backgroundLabel.add(CREATORS);
        backgroundLabel.add(HOW_TO_PLAY);
        backgroundLabel.add(EXIT);
        backgroundLabel.add(HIGH_SCORE);

        START.addActionListener(this);
        START.setFocusable(false);
        CREATORS.addActionListener(this);
        CREATORS.setFocusable(false);
        HOW_TO_PLAY.addActionListener(this);
        HOW_TO_PLAY.setFocusable(false);
        EXIT.addActionListener(this);
        EXIT.setFocusable(false);
        HIGH_SCORE.addActionListener(this);
        HIGH_SCORE.setFocusable(false);

        setTitle("River Raid Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(START)) {
            new Levels();
            setVisible(false);
        }
        if (e.getSource().equals(CREATORS)) {
            JOptionPane.showMessageDialog(this,
                    "1) Mohamed Mahmoud \n" +
                            "2) Hassan Fouad \n" +
                            "3) Hassan Adel \n" +
                            "4) Mostafa Elsabagh \n" +
                            "5) Fares Maged \n"
            );
        }
        if (e.getSource().equals(HOW_TO_PLAY)) {
            JOptionPane.showMessageDialog(this,
                    "1) Use the left right button to move the plane \n"
                            + "2) press Space to shoot bullets.\n"
                            + "3) UP to make the game speed up\n"
                            + "4) 100 points for hit ship\n "
                            + "5) 50 points for hit plane\n "
                            + "6) if hit fuel every enemy in it is zone will destroyed\n" + " and  you will git 30 points\n "
                            + "7) Try to survive and overcome the enemy."
            );
        }
        if (e.getSource().equals(HIGH_SCORE)) {
            // HIGH SCORE DASHBOARD:
        }
        if (e.getSource().equals(EXIT)) {
            setVisible(false);
            CLIP.close();
        }


    }
}