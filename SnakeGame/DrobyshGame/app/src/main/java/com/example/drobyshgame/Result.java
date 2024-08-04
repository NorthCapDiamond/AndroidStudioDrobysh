package com.example.drobyshgame;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Time;

@Entity
public class Result {
    @PrimaryKey
    public long id;
    public int score;
    public long duration;
}
