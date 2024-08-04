package com.example.drobyshgame;

import java.util.Timer;
import java.util.TimerTask;

public class SnakeTimer extends Timer {
    private final Timer timer;
    private boolean isRunning;

    public SnakeTimer(){
        this.timer = new Timer();
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void run(TimerTask timerTask, long delay, long period){
        isRunning = true;
        timer.schedule(timerTask, delay, period);
    }

    public void stop(){
        isRunning = false;
        timer.purge();
        timer.cancel();
    }
}
