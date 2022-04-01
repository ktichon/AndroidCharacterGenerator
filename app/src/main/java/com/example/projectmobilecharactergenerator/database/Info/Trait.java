package com.example.projectmobilecharactergenerator.database.Info;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Background.class,
        parentColumns = "id",
        childColumns = "background_id",
        onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = TraitType.class,
                parentColumns = "id",
                childColumns = "trait_type_id",
                onDelete = ForeignKey.CASCADE)
})
public class Trait {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public Long background_id;
    public Long trait_type_id;
    public String trait;

    public Trait(Long background_id, Long trait_type_id, String trait) {
        this.background_id = background_id;
        this.trait_type_id = trait_type_id;
        this.trait = trait;
    }
}
