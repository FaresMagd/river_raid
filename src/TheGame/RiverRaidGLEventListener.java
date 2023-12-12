package TheGame;

import Texture.TextureReader;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import models.HighScoreDTO;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

public class RiverRaidGLEventListener extends RiverRaidListener {

    JFrame gameJframe;

    public void setGameJframe(JFrame gameJframe) {
        this.gameJframe = gameJframe;
    }

    double speedFactor;

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }

    public void setSpeed(double speed){
        _speed = speed;
    }

    public int getSpeed(){
        return (int) Math.ceil(_speed * speedFactor);
    }

    String userName = null;

    GLUT g = new GLUT();
    public BitSet keyBits = new BitSet(256);

    int score;
    ArrayList<GameObject> plans = new ArrayList<>();
    ArrayList<GameObject> ships = new ArrayList<>();
    ArrayList<GameObject> homes = new ArrayList<>();
    ArrayList<GameObject> fulls = new ArrayList<>();

    int xBullet;
    int yBullet;
    boolean paused;
    int starttemp;
    boolean flag;
    long timeStart;
    long currentTime;
    private double _speed;

    long tts;
    long tte;
    long fts;
    long fte;
    long pts;
    long pte;
    long hts;
    long hte;
    boolean fired;
    int lives = 3;
    int tank = 100;
    int planeIndex = 4;
    int maxWidth = 100;
    int maxHeight = 100;
    int x = 45, y = 10;
    String[] textureNames = {"Eboot.png", "Eplane.png", "back.png", "plane_left.png", "plane_normal.png",
            "plane_right.png", "house.png", "bullet.png", "full.png", "startback.png"};
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];

    TextRenderer textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 24));

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D); // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                // mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }

        long start = System.currentTimeMillis();
        hts = start;
        pts = start;
        timeStart = start;
        fts = start;
        tte = start;


    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        setPaused();
        DrawBackground(gl);
        drawStart(gl);

        if (!paused) {
            handleKeyPress();
            generateAPlaneOrShip();
            generateHome();
            generateFull();
            changePos();
            checkTank();
            Destroy.destroy(this);
            Destroy.remove(this);
            emptyTank();
        }
        drawPlansAndShipsAndHomes(gl);
        DrawSprite(gl, x, y, planeIndex, 0.8F, 0);

        if (lives > 0)
            currentTime = System.currentTimeMillis();
        gl.glRasterPos2f(-.8f, .9f);
        g.glutBitmapString(5, "Score ");
        g.glutBitmapString(5, Integer.toString(score));

        gl.glRasterPos2f(-.8f, .8f);
        g.glutBitmapString(5, "Timer  ");
        g.glutBitmapString(5, Long.toString((currentTime - timeStart) / 1000));
        g.glutBitmapString(5, " s");

        gl.glRasterPos2f(-.8f, .7f);
        g.glutBitmapString(5, "lives  ");
        g.glutBitmapString(5, Integer.toString(lives));

        gl.glRasterPos2f(-.8f, .6f);
        g.glutBitmapString(5, "tank  ");
        g.glutBitmapString(5, Integer.toString(tank));
        gl.glEnd();
        gl.glRasterPos2f(-.8f, .84f);
        if (getSpeed() < 10)
            setSpeed(2 + score / 4000);
        starttemp++;
        starttemp = Math.min(starttemp, 150);
        if (fired) {
            DrawSprite(gl, xBullet, yBullet, 7, 0.1f, 0);
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale, float angle) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]); // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glRotatef(angle, 0, 1, 0);

        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        // System.out.println(x + " " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]); // Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void drawStart(GL gl) {
        DrawSprite(gl, 45, 45 - starttemp, 9, 10, 0);

    }

    public void generateAPlaneOrShip() {

        pte = System.currentTimeMillis();
        if ((pte - pts) + getSpeed() * 200L > 1500) {
            pts = pte;
            int temp = (int) (Math.random() * 44 + 23);
            double temp1 = Math.random();
            if (temp1 < 0.5) {
                plans.add(new GameObject(temp, Math.random() > 0.5));
            }
            if (temp1 > 0.5) {
                ships.add(new GameObject(temp, Math.random() > 0.5));

            }
        }
    }

    public void generateFull() {

        fte = System.currentTimeMillis();
        if ((fte - fts) + getSpeed() * 200L > 5000) {
            fts = fte;
            int temp = (int) (Math.random() * 44 + 23);
            fulls.add(new GameObject(temp, Math.random() > 0.5));
        }
    }

    public void emptyTank() {
        tte = System.currentTimeMillis();
        if (tte - tts > 400) {
            tts = tte;

            tank -= 1;
            tank = Math.max(tank, 0);
        }
    }

    public void checkTank() {
        if (tank < 1) {
            crashed();
        }
    }

    public void crashed() {
        x = 45;
        y = 10;
        tank = 100;
        lives -= 1;
        plans = new ArrayList<>();
        ships = new ArrayList<>();
        homes = new ArrayList<>();
        fulls = new ArrayList<>();
        if (lives == 0) {
            paused = true;
            updateHighScore();
            String MSG = "Your score is " + score + " Do you want to play again";
            int RET = JOptionPane.showConfirmDialog(null, MSG, "Game Over!", JOptionPane.YES_NO_OPTION);
            if (RET == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                this.gameJframe.setVisible(false);
                new RiverRaid();

            }
        }
        if (lives > 0)
            starttemp = 0;

    }

    public void updateHighScore() {
        List<HighScoreDTO> users = new ArrayList<HighScoreDTO>();
        try {
            File file = new File("src/Assets/high_score.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] parts = line.split(" ");
                StringBuilder name = new StringBuilder();
                int score = 0;
                for (String part : parts) {
                    if (part.length() > 1 && part.charAt(0) >= '0' && part.charAt(0) <= '9') {
                        score = Integer.parseInt(part);
                        break;
                    }
                    name.append(part).append(" ");
                }
                users.add(new HighScoreDTO(name.toString(), score));
            }
            scanner.close();
            if (userName != null) {
                users.add(new HighScoreDTO(userName , score));
            }
            try {

                users.sort((o1,o2)-> o2.score - o1.score);
                FileWriter myWriter = new FileWriter("src/Assets/high_score.txt");


                int num = 1;
                for (HighScoreDTO pair : users) {
                    String first = pair.name;
                    int second = pair.score;
                    myWriter.write(first + " " + second + "\n");
                    num++;
                    if (num == 11) {
                        break;
                    }
                }
                myWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException err) {
            err.printStackTrace();
        }
    }

    public void newGame() {
        lives = 3;
        tank = 100;
        score = 0;
        flag = true;
        plans = new ArrayList<>();
        ships = new ArrayList<>();
        homes = new ArrayList<>();
        fulls = new ArrayList<>();
        keyBits = new BitSet(256);

        long start = System.currentTimeMillis();
        hts = start;
        pts = start;
        timeStart = start;
        fts = start;
        tte = start;
        paused = false;
        x = 45;
        planeIndex = 4;
    }

    public void drawPlansAndShipsAndHomes(GL gl) {

        for (GameObject plan : plans) {
            DrawSprite(gl, plan.x, plan.y, 1, 1, plan.left ? 0 : 180);
        }
        for (GameObject ship : ships) {
            DrawSprite(gl, ship.x, ship.y, 0, 1, ship.left ? 180 : 0);
        }
        for (GameObject home : homes) {
            DrawSprite(gl, home.x, home.y, 6, 1.5f, home.left ? 0 : 180);
        }
        for (GameObject full : fulls) {
            DrawSprite(gl, full.x, full.y, 8, 1, full.left ? 0 : 180);
        }

    }

    public void generateHome() {

        hte = System.currentTimeMillis();
        if ((hte - hts) + getSpeed() * 300L > 3000) {
            hts = hte;
            int temp;
            if (Math.random() > 0.5) {
                temp = (int) (Math.random() * 11 + 1);
            } else {
                temp = (int) (Math.random() * 11 + 78);
            }
            homes.add(new GameObject(temp, Math.random() > 0.5));

        }
    }

    public void changePos() {
        for (GameObject plan : plans) {
            plan.y -= getSpeed();
            if (plan.y < 50) {
                if (plan.left && plan.x > 67) {
                    plan.left = false;
                }
                if (!plan.left && plan.x < 23) {
                    plan.left = true;
                }
                if (plan.left) {
                    plan.x += 2;
                } else {
                    plan.x -= 2;
                }
            }
            if (plan.y < 0) {
                plan.remove = true;
            }
        }
        for (GameObject ship : ships) {
            ship.y -= getSpeed();
            if (ship.y < 50) {
                if (ship.left && ship.x > 67) {
                    ship.left = false;
                }
                if (!ship.left && ship.x < 23) {
                    ship.left = true;
                }
                if (ship.left) {
                    ship.x++;
                } else {
                    ship.x--;
                }
            }
            if (ship.y < 0) {
                ship.remove = true;
            }
        }
        for (GameObject home : homes) {
            home.y -= getSpeed();
            if (home.y < 0) {
                home.remove = true;
            }
        }
        for (GameObject full : fulls) {
            full.y -= getSpeed();
            if (full.y < 0) {
                full.remove = true;
            }
        }
        if (fired) {
            yBullet += 10;
        }
    }

    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (x > 20) {
                x -= 3;
                planeIndex = 3;
            }
        } else if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (x < maxWidth - 30) {
                x += 3;
                planeIndex = 5;
            }
        } else {
            planeIndex = 4;
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            if (yBullet > 100 || !fired) {
                xBullet = x;
                yBullet = 10;
                fired = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            setSpeed(getSpeed() + 2);
        }

    }

    public void setPaused() {
        if (isKeyPressed(KeyEvent.VK_P) && lives > 0) {
            paused = !paused;
        }
    }


    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);

    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care
    }

}
