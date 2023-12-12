package TheGame;

import Config.Constants;
import Texture.TextureReader;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import models.GameObject;
import models.GameState;
import models.HighScoreDTO;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
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

import static Config.Constants.*;

public class RiverRaidGLEventListener extends KeyHandling implements GLEventListener {
    JFrame gameJframe;
    GameState gameState;
    GameState firstPlayer , SecondPlayer;
    public void setGameJframe(JFrame gameJframe) {
        this.gameJframe = gameJframe;
        this.gameState = new GameState();
    }

    public void setUserName(String username){
        this.gameState.userName = username;
    }
    public void setSpeedFactor(double speedFactor) {
        this.gameState.speedFactor = speedFactor;
    }

    GLUT g = new GLUT();
    public BitSet keyBits = new BitSet(256);

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
                texture[i] = TextureReader.readTexture(Constants.assetsFolderName + "//" + textureNames[i], true);
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
        this.gameState.LastHomeGeneratedTime = start;
        this.gameState.LastPlaneGenerateTime = start;
        this.gameState.timeStart = start;
        this.gameState.fts = start;
        this.gameState.tte = start;


    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        setPaused();
        DrawBackground(gl);
        drawStart(gl);

        if (!this.gameState.paused) {
            handleKeyPress();
            generateAPlaneOrShip();
            generateHome();
            generateFull();
            changePos();
            checkTank();
            CollisionManager.destroy(this);
            CollisionManager.remove(this);
            emptyTank();
        }
        drawPlansAndShipsAndHomes(gl);
        DrawSprite(gl, this.gameState.x, this.gameState.y, this.gameState.planeIndex, 0.8F, 0);

        if (this.gameState.lives > 0)
            this.gameState.currentTime = System.currentTimeMillis();
        gl.glRasterPos2f(-.8f, .9f);
        g.glutBitmapString(5, "Score ");
        g.glutBitmapString(5, Integer.toString(this.gameState.score));

        gl.glRasterPos2f(-.8f, .8f);
        g.glutBitmapString(5, "Timer  ");
        g.glutBitmapString(5, Long.toString((this.gameState.currentTime - this.gameState.timeStart) / 1000));
        g.glutBitmapString(5, " s");

        gl.glRasterPos2f(-.8f, .7f);
        g.glutBitmapString(5, "lives  ");
        g.glutBitmapString(5, Integer.toString(this.gameState.lives));

        gl.glRasterPos2f(-.8f, .6f);
        g.glutBitmapString(5, "tank  ");
        g.glutBitmapString(5, Integer.toString(this.gameState.tank));
        gl.glEnd();
        gl.glRasterPos2f(-.8f, .84f);
        if (this.gameState.getSpeed() < 10)
            this.gameState.setSpeed(2 + this.gameState.score / 4000);
        this.gameState.starttemp++;
        this.gameState.starttemp = Math.min(this.gameState.starttemp, 150);
        if (this.gameState.fired) {
            DrawSprite(gl, this.gameState.xBullet, this.gameState.yBullet, 7, 0.1f, 0);
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
        DrawSprite(gl, 45, 45 - this.gameState.starttemp, 9, 10, 0);

    }

    public void generateAPlaneOrShip() {

        this.gameState.pte = System.currentTimeMillis();
        if (( this.gameState.pte -  this.gameState.LastPlaneGenerateTime) + this.gameState.getSpeed() * 200L > 1500) {
            this.gameState.LastPlaneGenerateTime = this.gameState.pte;
            int temp = (int) (Math.random() * 44 + 23);
            double temp1 = Math.random();
            if (temp1 < 0.5) {
                this.gameState.plans.add(new GameObject(temp, Math.random() > 0.5));
            }
            if (temp1 > 0.5) {
                this.gameState.ships.add(new GameObject(temp, Math.random() > 0.5));

            }
        }
    }

    public void generateFull() {

        this.gameState.fte = System.currentTimeMillis();
        if ((this.gameState.fte - this.gameState.fts) + this.gameState.getSpeed() * 200L > 5000) {
            this.gameState.fts = this.gameState.fte;
            int temp = (int) (Math.random() * 44 + 23);
            this.gameState.fulls.add(new GameObject(temp, Math.random() > 0.5));
        }
    }

    public void emptyTank() {
        this.gameState.tte = System.currentTimeMillis();
        if (this.gameState.tte - this.gameState.tts > 400) {
            this.gameState.tts = this.gameState.tte;

            this.gameState.tank -= 1;
            this.gameState.tank = Math.max(this.gameState.tank, 0);
        }
    }

    public void checkTank() {
        if (this.gameState.tank < 1) {
            crashed();
        }
    }

    public void crashed() {

        this.gameState.x = 45;
        this.gameState.y = 10;
        this.gameState.tank = 100;
        this.gameState.lives -= 1;
        this.gameState.plans = new ArrayList<>();
        this.gameState.ships = new ArrayList<>();
        this.gameState.homes = new ArrayList<>();
        this.gameState.fulls = new ArrayList<>();
        if (this.gameState.lives == 0) {
            if(this.gameState.isMultipalyer && this.gameState.currentPlayer == 1){
                this.startSecondPlayer();
                return;
            } else if (this.gameState.isMultipalyer){
                int firstPlayerScore = this.firstPlayer.score;
                int secondPlayerScore = this.gameState.score;
                String MSG = "first player score "+firstPlayerScore+" and second player score "+ secondPlayerScore+"\n the winner is "+(firstPlayerScore>secondPlayerScore?"first player":"second player");

                int RET = JOptionPane.showConfirmDialog(null,MSG , "Game Over!", JOptionPane.YES_NO_OPTION);
                return;
            }
            this.gameState.paused = true;
            updateHighScore();
            String MSG = "Your score is " + this.gameState.score + " Do you want to play again";
            int RET = JOptionPane.showConfirmDialog(null, MSG, "Game Over!", JOptionPane.YES_NO_OPTION);
            if (RET == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                this.gameJframe.setVisible(false);
                new RiverRaid();

            }
        }
        if (this.gameState.lives > 0)
            this.gameState.starttemp = 0;

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
            if (this.gameState.userName != null) {
                users.add(new HighScoreDTO(this.gameState.userName , this.gameState.score));
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
        this.gameState.lives = 3;
        this.gameState.tank = 100;
        this.gameState.score = 0;
        this.gameState.flag = true;
        this.gameState.plans = new ArrayList<>();
        this.gameState.ships = new ArrayList<>();
        this.gameState.homes = new ArrayList<>();
        this.gameState.fulls = new ArrayList<>();
        keyBits = new BitSet(256);

        long start = System.currentTimeMillis();
        this.gameState.LastHomeGeneratedTime = start;
        this.gameState.LastPlaneGenerateTime = start;
        this.gameState.timeStart = start;
        this.gameState.fts = start;
        this.gameState.tte = start;
        this.gameState.paused = false;
        this.gameState.x = 45;
        this.gameState.planeIndex = 4;
    }

    public void drawPlansAndShipsAndHomes(GL gl) {

        for (GameObject plan : this.gameState.plans) {
            DrawSprite(gl, plan.x, plan.y, 1, 1, plan.left ? 0 : 180);
        }
        for (GameObject ship : this.gameState.ships) {
            DrawSprite(gl, ship.x, ship.y, 0, 1, ship.left ? 180 : 0);
        }
        for (GameObject home : this.gameState.homes) {
            DrawSprite(gl, home.x, home.y, 6, 1.5f, home.left ? 0 : 180);
        }
        for (GameObject full : this.gameState.fulls) {
            DrawSprite(gl, full.x, full.y, 8, 1, full.left ? 0 : 180);
        }

    }

    public void generateHome() {

        this.gameState.hte = System.currentTimeMillis();
        if ((this.gameState.hte - this.gameState.LastHomeGeneratedTime) + this.gameState.getSpeed() * 300L > 3000) {
            this.gameState.LastHomeGeneratedTime = this.gameState.hte;
            int temp;
            if (Math.random() > 0.5) {
                temp = (int) (Math.random() * 11 + 1);
            } else {
                temp = (int) (Math.random() * 11 + 78);
            }
            this.gameState.homes.add(new GameObject(temp, Math.random() > 0.5));

        }
    }

    public void changePos() {
        for (GameObject plan : this.gameState.plans) {
            plan.y -= this.gameState.getSpeed();
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
        for (GameObject ship : this.gameState.ships) {
            ship.y -= this.gameState.getSpeed();
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
        for (GameObject home : this.gameState.homes) {
            home.y -= this.gameState.getSpeed();
            if (home.y < 0) {
                home.remove = true;
            }
        }
        for (GameObject full : this.gameState.fulls) {
            full.y -= this.gameState.getSpeed();
            if (full.y < 0) {
                full.remove = true;
            }
        }
        if (this.gameState.fired) {
            this.gameState.yBullet += 10;
        }
    }

    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (this.gameState.x > 20) {
                this.gameState.x -= 3;
                this.gameState.planeIndex = 3;
            }
        } else if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (this.gameState.x < maxWidth - 30) {
                this.gameState.x += 3;
                this.gameState.planeIndex = 5;
            }
        } else {
            this.gameState.planeIndex = 4;
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            if (this.gameState.yBullet > 100 || !this.gameState.fired) {
                this.gameState.xBullet = this.gameState.x;
                this.gameState.yBullet = 10;
                this.gameState.fired = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            this.gameState.setSpeed(this.gameState.getSpeed() + 2);
        }

    }

    public void setPaused() {
        if (isKeyPressed(KeyEvent.VK_P) && this.gameState.lives > 0) {
            this.gameState.paused = !this.gameState.paused;
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

    public void setSecondPlayer(String secondPlayerName) {
        this.gameState.SecondPlayerUserName = secondPlayerName;
    }

    public void setMultiplayer(boolean b) {
        this.gameState.isMultipalyer = true;
    }

    public void startSecondPlayer(){
        this.firstPlayer = new GameState();
        firstPlayer.currentPlayer = 1;
        firstPlayer.userName = this.gameState.userName;
        firstPlayer.score = this.gameState.score;
        String secondPlayerName = this.gameState.SecondPlayerUserName;
        this.gameState = new GameState();
        this.gameState.userName = secondPlayerName;
        this.gameState.currentPlayer = 2;
        this.keyBits = new BitSet(256);
        String MSG = "HI " + secondPlayerName + " , Are You Ready?";
        int RET = JOptionPane.showConfirmDialog(null, MSG, "Ready MSG?", JOptionPane.YES_NO_OPTION);
    }
}
