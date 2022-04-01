package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Epithet.class, Background.class, TraitType.class, Trait.class }, version = 2)
public abstract class InfoDatabase extends RoomDatabase {
    public abstract EpithetDao epithetDao();
    public abstract BackgroundDao backgroundDao();
    public abstract TraitTypeDao traitTypeDao();
    public abstract TraitDao traitDao();
}