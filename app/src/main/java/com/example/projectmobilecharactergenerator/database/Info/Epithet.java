package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Epithet implements Comparable<Epithet> {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String source;
    public String epithet;

    public Epithet(String source, String epithet) {
        this.source = source;
        this.epithet = epithet;
    }

    @Override
    public int compareTo(Epithet epithet) {
        return this.epithet.compareTo(epithet.epithet);
    }
}
