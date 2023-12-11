package TheGame;

import Texture.TextureReader;
import com.sun.opengl.util.j2d.TextRenderer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class RiverRaidGLEventListener extends RiverRaidListener {
    int score;
    ArrayList<Object1> plans = new ArrayList<>();
    ArrayList<Object1> ships = new ArrayList<>();
    ArrayList<Object1> homes = new ArrayList<>();
    ArrayList<Object1> fulls = new ArrayList<>();
    int xBullet;
    int yBullet;
    boolean paused;
    int starttemp;

    int speed;

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
    String[] textureNames = {"Eboot.png", "Eplane.png", "back.png", "plane_left.png", "plane_normal.png", "plane_right.png", "house.png", "bullet.png", "full.png", "startback.png"};
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    TextRenderer textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 24));

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
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
        fts = start;
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        setPaused();
        DrawBackground(gl);

        if (!paused) {
            handleKeyPress();
            generateAPlaneOrShip();
            generateHome();
            generateFull();
            changePos();
        }
        drawPlansAndShipsAndHomes(gl);
        DrawSprite(gl, x, y, planeIndex, 0.8F, 0);
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
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glRotatef(angle, 0, 1, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
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
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);    // Turn Blending On
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


    public void generateAPlaneOrShip() {
        pte = System.currentTimeMillis();
        if ((pte - pts) + speed * 200L > 1500) {
            pts = pte;
            int temp = (int) (Math.random() * 44 + 23);
            double temp1 = Math.random();
            if (temp1 < 0.5) {
                plans.add(new Object1(temp, Math.random() > 0.5));
            }
            if (temp1 > 0.5) {
                ships.add(new Object1(temp, Math.random() > 0.5));
            }
        }
    }

    public void generateFull() {
        fte = System.currentTimeMillis();
        if ((fte - fts) + speed * 200L > 5000) {
            fts = fte;
            int temp = (int) (Math.random() * 44 + 23);
            fulls.add(new Object1(temp, Math.random() > 0.5));
        }
    }


    public void crashed() {
        x = 45;
        y = 10;
        tank = 100;
        lives -= 1;
        if (lives == 0) {
            paused = true;
        }
        starttemp = 0;
    }

    public void drawPlansAndShipsAndHomes(GL gl) {

        for (Object1 plan : plans) {
            DrawSprite(gl, plan.x, plan.y, 1, 1, plan.left ? 0 : 180);
        }
        for (Object1 ship : ships) {
            DrawSprite(gl, ship.x, ship.y, 0, 1, ship.left ? 180 : 0);
        }
        for (Object1 home : homes) {
            DrawSprite(gl, home.x, home.y, 6, 1.5f, home.left ? 0 : 180);
        }
        for (Object1 full : fulls) {
            DrawSprite(gl, full.x, full.y, 8, 1, full.left ? 0 : 180);
        }
    }

    public void generateHome() {

        hte = System.currentTimeMillis();
        if ((hte - hts) + speed * 300L > 3000) {
            hts = hte;
            int temp;
            if (Math.random() > 0.5) {
                temp = (int) (Math.random() * 11 + 1);
            } else {
                temp = (int) (Math.random() * 11 + 78);
            }
            homes.add(new Object1(temp, Math.random() > 0.5));

        }
    }

    public void changePos() {
        for (Object1 plan : plans) {
            plan.y -= 2;
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
        for (Object1 ship : ships) {
            ship.y -= 2;
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
        for (Object1 home : homes) {
            home.y -= 2;
            if (home.y < 0) {
                home.remove = true;
            }
        }
        for (Object1 full : fulls) {
            full.y -= 2;
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


    }

    public void setPaused() {
        if (isKeyPressed(KeyEvent.VK_P) && lives > 0) {
            paused = !paused;
        }
    }

}

