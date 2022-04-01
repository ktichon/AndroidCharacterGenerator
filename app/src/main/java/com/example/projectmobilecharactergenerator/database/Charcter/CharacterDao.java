package com.example.projectmobilecharactergenerator.database.Charcter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CharacterDao {
    @Insert
    long insertCharacter(CharacterInfo character);

    @Update
    public void updateCharacter(CharacterInfo character);

    @Delete
    void deleteCharacter(CharacterInfo character);

    @Query("SELECT * FROM CharacterInfo")
    List<CharacterInfo> getAllCharacters();

    @Query("SELECT * FROM CharacterInfo WHERE id = :id")
    CharacterInfo getCharacter(Long id);



}
