package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TraitType {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    String type;

    public TraitType(String type) {
        this.type = type;
    }
}
