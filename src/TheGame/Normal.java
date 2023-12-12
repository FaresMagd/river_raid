package TheGame;

//import Soldier.Anim;
//import Soldier.AnimGLEventListener;
//import Soldier.AnimListener;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class Normal extends JFrame {
    public Normal() {
        GLCanvas glcanvas;
        Animator animator;

        RiverRaidGLEventListener listener = new RiverRaidGLEventListener();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);

        glcanvas.addKeyListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(15);
        animator.add(glcanvas);
        animator.start();

        setTitle("River Raid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }

    public static void main(String[] args) {

        new RiverRaid();
    }
}
