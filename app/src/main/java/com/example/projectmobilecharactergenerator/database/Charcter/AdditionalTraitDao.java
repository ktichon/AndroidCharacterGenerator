package com.example.projectmobilecharactergenerator.database.Charcter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.projectmobilecharactergenerator.database.Info.Trait;

import java.util.List;

@Dao
public interface AdditionalTraitDao {
        @Insert
        void insertAdditionalTrait(AdditionalTrait trait);

        @Update
        public void updateAdditionalTrait(AdditionalTrait trait);

        @Delete
        void deleteAdditionalTrait(AdditionalTrait trait);

        @Insert
        void insertAdditionalTraitList(List<AdditionalTrait> AdditionalTrait);

        @Query("SELECT * FROM AdditionalTrait")
        List<AdditionalTrait> getAllAdditionalTrait();

        @Query("SELECT * FROM AdditionalTrait WHERE id = :id")
        AdditionalTrait getAdditionalTrait(Long id);

        @Query("SELECT * FROM AdditionalTrait WHERE character_id = :character_id ORDER BY displayOrder ASC")
        List<AdditionalTrait> getAllAdditionalTraitsFromCharacter(Long character_id);

        @Query("DELETE FROM AdditionalTrait WHERE character_id = :character_id")
        void deleteAllAdditionalTraitsFromCharacter(long character_id);

        @RawQuery
        List<AdditionalTrait> getAdditionalTraitByQuery(SupportSQLiteQuery query);

        @Query("DELETE FROM AdditionalTrait")
        void deleteAll();
    }
