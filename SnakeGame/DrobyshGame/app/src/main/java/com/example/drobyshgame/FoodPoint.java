package com.example.drobyshgame;

public class FoodPoint extends Point{

    private int music;
    private int color;
    private int score;

    public void setMusic(int music) {
        this.music = music;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public FoodPoint(int x, int y, int score, int color, int music) {
        super(x, y);
        this.color = color;
        this.music = music;
        this.score = score;
    }

    public FoodPoint(Point p, int score, int color, int music) {
        super(p);
        this.color = color;
        this.music = music;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getColor() {
        return color;
    }

    public int getMusic() {
        return music;
    }
}
