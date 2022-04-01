package com.example.projectmobilecharactergenerator.database.Charcter;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CharacterInfo {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public boolean hasVillain;
    public boolean hasDnDCharacter;
    public String name;
    public String epithet;
    public String art_path;
    public String description;

    public CharacterInfo(String name, String epithet, String art_path, boolean hasVillain, boolean hasDnDCharacter, String description) {
        this.hasVillain = hasVillain;
        this.hasDnDCharacter = hasDnDCharacter;
        this.name = name;
        this.art_path = art_path;
        this.epithet = epithet;
        this.description = description;
    }
}
