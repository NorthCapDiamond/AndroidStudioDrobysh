package com.example.drobyshgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;

public class Game {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int moveSide = 4;
    private TextView scoreView;
    private SnakeTimer snakeTimer;
    private ArrayList<FoodPoint> foodTypes;
    private LinkedList<Point> snakePoints;
    private int score;
    private Paint paint;
    private Canvas canvas;
    private int snakeColor = Color.GREEN;
    private int snakeSpeed = 500;
    private int startSpeed = 500;
    private int pointSize = 30;
    private FoodPoint foodPoint;
    private int needToIncrease = 0;
    private MediaPlayer player;
    Point extraPoint;
    private long startTime;

    public void setMoveSide(int side){
        moveSide = side;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public int getMoveSide() {
        return moveSide;
    }

    public int getScore() {
        return score;
    }

    public Game(SurfaceView surfaceView, TextView scoreView){
        this.scoreView = scoreView;
        this.surfaceView = surfaceView;
        this.snakePoints = new LinkedList<>();
        this.foodTypes = new ArrayList<>();
        foodPoint = new FoodPoint(0,0,0,0,0);
    }

    public SnakeTimer getSnakeTimer() {
        return snakeTimer;
    }

    public void initGame(FoodPoint ... types) {

        foodTypes.clear();
        snakePoints.clear();
        snakeSpeed = startSpeed;
        score = new Random().nextInt(4)+1;
        scoreView.setText(String.valueOf(score));
        moveSide = 4;
        if(snakeTimer!=null){
            if(snakeTimer.isRunning()){
                snakeTimer.stop();
            }
        }

        Collections.addAll(foodTypes, types);
        for(int i = 0; i < score; i++){
            snakePoints.add(new Point(((2*(score-i)-1)*pointSize),pointSize));
        }

        createFood();
        startTime = System.currentTimeMillis();
        move();
    }

    public void createFood(){

        int maxWidthCounter = surfaceView.getWidth()/(2*pointSize);
        int maxHeightCounter = surfaceView.getHeight()/(2*pointSize);

        int randomX = new Random().nextInt(maxWidthCounter);
        int randomY = new Random().nextInt(maxHeightCounter);
        int randomZ = new Random().nextInt(foodTypes.size());
        randomX = (2*(maxWidthCounter-randomX)-1)*pointSize;
        randomY = (2*(maxHeightCounter-randomY)-1)*pointSize;

        foodPoint.setX(randomX);
        foodPoint.setY(randomY);
        foodPoint.setScore(foodTypes.get(randomZ).getScore());
        foodPoint.setColor(foodTypes.get(randomZ).getColor());
        foodPoint.setMusic(foodTypes.get(randomZ).getMusic());

    }

    public void move(){
        snakeTimer = new SnakeTimer();
        snakeTimer.run(new TimerTask() {
            @Override
            public void run() {
                if(needToIncrease > 0){
                    increaseSnake();
                }
                Point head = snakePoints.getFirst();

                if(head.getX() == foodPoint.getX() && head.getY() == foodPoint.getY()){
                    needToIncrease+=foodPoint.getScore();
                    increaseSnake();
                    playMusic(foodPoint.getMusic());
                    createFood();

                    if (snakeSpeed < 800) {
                        snakeSpeed += 9;
                        snakeTimer.stop();
                        move();
                    }
                }


                extraPoint = new Point(head);
                switch (moveSide){
                    case 1:
                        head.setX(head.getX()-pointSize*2);
                        break;
                    case 2:
                        head.setY(head.getY()+2*pointSize);
                        break;
                    case 3:
                        head.setY(head.getY()-2*pointSize);
                        break;
                    case 4:
                        head.setX(head.getX()+pointSize*2);
                        break;
                }

                int tempWidth = surfaceView.getWidth()/(2*pointSize);;
                int tempHeight = surfaceView.getHeight()/(2*pointSize);
                if(head.getX() < pointSize){
                    head.setX((2*tempWidth-1)*pointSize);
                }
                if(head.getY() < pointSize){
                    head.setY((2*tempHeight-1)*pointSize);
                }
                if(head.getX() > (2*tempWidth-1)*pointSize){
                    head.setX(pointSize);
                }
                if(head.getY() > (2*tempHeight-1)*pointSize){
                    head.setY(pointSize);
                }

                if(isCrossed()){
                    Result tmp = new Result();
                    tmp.id = 0;
                    tmp.duration = (System.currentTimeMillis()-startTime)/1000;
                    tmp.score = score;
                    MainActivity.getInstance().liveData.postValue(tmp);
                    MainActivity.getInstance().ruinedGame();
                }
                else {
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    Paint tmPaint = new Paint();
                    tmPaint.setColor(foodPoint.getColor());
                    tmPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(foodPoint.getX(), foodPoint.getY(), pointSize, tmPaint);
                    canvas.drawCircle(head.getX(), head.getY(), pointSize, getPaintColor());


                    int tmpX;
                    int tmpY;
                    for(int i = 1; i < snakePoints.size(); i++){
                        tmpX = snakePoints.get(i).getX();
                        tmpY = snakePoints.get(i).getY();
                        snakePoints.get(i).setX(extraPoint.getX());
                        snakePoints.get(i).setY(extraPoint.getY());

                        canvas.drawCircle(snakePoints.get(i).getX(), snakePoints.get(i).getY(), pointSize, getPaintColor());

                        extraPoint = new Point(tmpX, tmpY);

                    }
                    //canvas.drawCircle(foodPoint.getX(), foodPoint.getY(), pointSize, tmPaint);
                    surfaceHolder.unlockCanvasAndPost(canvas);


                }


            }
        }, 1000-snakeSpeed, 1000-snakeSpeed);
    }


    public void increaseSnake(){
        Point point = new Point(-pointSize, -pointSize);
        snakePoints.add(point);
        MainActivity.getInstance().updateScore(++score);
        needToIncrease--;
    }

    public void playMusic(int music){
        if(player==null){
            player = MediaPlayer.create(MainActivity.getInstance().getApplicationContext(), music);
            player.start();
        }
        else {
            player.stop();
            player = MediaPlayer.create(MainActivity.getInstance().getApplicationContext(), music);
            player.start();
        }
    }

    public boolean isCrossed(){
        Point head = snakePoints.getFirst();
        for(int i = 1; i < snakePoints.size(); i++){
            if(head.getX() == snakePoints.get(i).getX() && head.getY() == snakePoints.get(i).getY()){
                playMusic(R.raw.last);
                return true;
            }
        }
        return false;
    }

    private Paint getPaintColor(){
        if (paint==null){
            paint = new Paint();
            paint.setColor(snakeColor);
            paint.setStyle(Paint.Style.FILL);
        }
        return paint;
    }
}
