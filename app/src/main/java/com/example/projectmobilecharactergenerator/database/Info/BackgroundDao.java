package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BackgroundDao {
    @Insert
    long insertBackground(Background background);

    @Update
    public void updateBackground(Background background);

    @Delete
    void deleteBackground(Background background);

    @Insert
    void insertBackgroundList(List<Background> backgrounds);

    @Query("SELECT * FROM background")
    List<Background> getAllBackground();

    @Query("SELECT * FROM background WHERE id = :id")
    Background getBackground(int id);

    @Query("DELETE FROM background")
    void deleteAll();

}