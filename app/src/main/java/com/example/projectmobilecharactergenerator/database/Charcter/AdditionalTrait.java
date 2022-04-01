package com.example.projectmobilecharactergenerator.database.Charcter;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = CharacterInfo.class,
        parentColumns = "id",
        childColumns = "character_id",
        onDelete = ForeignKey.CASCADE)
})
public class AdditionalTrait {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public Long character_id;
    public String characterTrait;
    public String traitCategory;
    public String traitType;
    public int displayOrder;

    public AdditionalTrait(Long character_id, String characterTrait, String traitCategory, String traitType, int displayOrder) {
        this.character_id = character_id;
        this.characterTrait = characterTrait;
        this.traitCategory = traitCategory;
        this.traitType = traitType;
        this.displayOrder = displayOrder;
    }
}
