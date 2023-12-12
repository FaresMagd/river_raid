package models;

import java.util.ArrayList;

public class GameState {
    public String userName = null;
    public int score;
    public ArrayList<GameObject> plans = new ArrayList<>();
    public ArrayList<GameObject> ships = new ArrayList<>();
    public ArrayList<GameObject> homes = new ArrayList<>();
    public ArrayList<GameObject> fulls = new ArrayList<>();

    public int xBullet;
    public int yBullet;
    public boolean paused;
    public int starttemp;
    public boolean flag;
    public long timeStart;
    public long currentTime;
    private double _speed;

    public long tts;
    public long tte;
    public long fts;
    public long fte;
    public long LastPlaneGenerateTime;
    public long pte;
    public long LastHomeGeneratedTime;
    public long hte;
    public boolean fired;
    public int lives = 3;
    public int tank = 100;
    public int planeIndex = 4;
    public int x = 45, y = 10;
    public double speedFactor;

    public void setSpeed(double speed){
        _speed = speed;
    }

    public int getSpeed(){
        return (int) Math.ceil(_speed * speedFactor);
    }
}
