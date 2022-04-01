package com.example.projectmobilecharactergenerator.storageClasses;

import com.example.projectmobilecharactergenerator.database.Charcter.AdditionalTrait;
import com.example.projectmobilecharactergenerator.database.Info.Trait;

import java.util.List;

public class SaveTraitParam {
    public List<AdditionalTrait> additionalTraitList;
    public long character_id;

    public SaveTraitParam(List<AdditionalTrait> additionalTraitList, long character_id) {
        this.additionalTraitList = additionalTraitList;
        this.character_id = character_id;
    }
}
