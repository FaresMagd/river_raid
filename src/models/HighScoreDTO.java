package models;

public class HighScoreDTO {
    public String name;

    public int score;
    public HighScoreDTO(String name, int score){
        this.name = name;
        this.score = score;
    }
}
