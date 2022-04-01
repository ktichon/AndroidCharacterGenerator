package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Background {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    String background;

    public Background(String background) {
        this.background = background;
    }
}
