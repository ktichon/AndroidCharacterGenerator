package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface TraitDao {
    @Insert
    void insertTrait(Trait trait);

    @Update
    public void updateTrait(Trait trait);

    @Delete
    void deleteTrait(Trait trait);

    @Insert
    void insertTraitList(List<Trait> traits);

    @Query("SELECT * FROM trait")
    List<Trait> getAllTrait();

    @Query("SELECT * FROM trait WHERE id = :id")
    Trait getTrait(Long id);

    @Query("SELECT * FROM trait WHERE background_id = :background_id AND trait_type_id = :trait_type_id")
    List<Trait> getTraitByBackgroundAndType(Long background_id, Long trait_type_id);

    @RawQuery
    List<Trait> getTraitsByQuery(SupportSQLiteQuery query);

    @Query("DELETE FROM trait")
    void deleteAll();
}