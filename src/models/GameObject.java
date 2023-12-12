package models;

public class GameObject {
    public int x;
    public int y;
    public boolean left;
    public boolean remove;

    public GameObject(int x, boolean left) {
        this.x = x;
        this.y = 100;
        this.left = left;
    }
    
}
