package com.example.drobyshgame;

import android.app.Application;
import androidx.room.Room;

public class DataApp extends Application {
    public static DataApp instance;
    private AppDataBase database;
    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDataBase.class, "Database").allowMainThreadQueries().build();
    }

    public static DataApp getApp(){
        return instance;
    }

    public AppDataBase getDatabase(){
        return database;
    }
}
