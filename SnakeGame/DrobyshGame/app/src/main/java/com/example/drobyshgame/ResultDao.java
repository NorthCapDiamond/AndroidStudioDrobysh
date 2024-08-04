package com.example.drobyshgame;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ResultDao {
    @Query("SELECT * FROM Result")
    List<Result> getAll();

    @Query("SELECT * FROM Result")
    LiveData<List<Result>> getAllLive();

    @Query("SELECT * FROM RESULT where id = :id")
    Result getById(long id);

    @Query("select * from result order by score desc")
    List<Result> orderByScore();

    @Query("Delete from result where score = (Select min(result.score) from result)")
    void deleteMinimum();

    @Insert
    void insert(Result result);

    @Update
    void update(Result result);

    @Delete
    void delete(Result result);

}
