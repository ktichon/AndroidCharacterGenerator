package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TraitTypeDao {
    @Insert
    Long insertTraitType(TraitType traittype);

    @Update
    public void updateTraitType(TraitType traittype);

    @Delete
    void deleteTraitType(TraitType traittype);

    @Insert
    void insertTraitTypeList(List<TraitType> traittypes);

    @Query("SELECT * FROM traittype")
    List<TraitType> getAllTraitType();

    @Query("SELECT * FROM traittype WHERE id = :id")
    TraitType getTraitType(long id);

    @Query("DELETE FROM traittype")
    void deleteAll();

}