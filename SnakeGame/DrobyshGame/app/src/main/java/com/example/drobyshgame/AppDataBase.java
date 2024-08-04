package com.example.drobyshgame;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.RoomDatabase;

@Database(entities = {Result.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract ResultDao resultDao();
}
