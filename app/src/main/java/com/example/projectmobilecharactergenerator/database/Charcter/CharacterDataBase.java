package com.example.projectmobilecharactergenerator.database.Charcter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CharacterInfo.class, AdditionalTrait.class}, version = 6)
public abstract class CharacterDataBase extends RoomDatabase {
    public abstract CharacterDao characterDao();
    public abstract AdditionalTraitDao additionalTraitDao();
}





