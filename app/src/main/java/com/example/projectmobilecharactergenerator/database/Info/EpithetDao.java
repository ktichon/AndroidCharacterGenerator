package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EpithetDao {
    @Insert
    void insertEpithet(Epithet epithet);

    @Update
    public void updateEpithet(Epithet epithet);

    @Delete
    void deleteEpithet(Epithet epithet);

    @Insert
    void insertEpithetList(List<Epithet> epithets);

    @Query("SELECT * FROM epithet")
    List<Epithet> getAllEpithet();

    @Query("SELECT * FROM epithet WHERE id = :id")
    Epithet getEpithet(int id);

    @Query("DELETE FROM epithet")
    public void deleteAll();

}