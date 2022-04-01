package com.example.projectmobilecharactergenerator.storageClasses;

import com.example.projectmobilecharactergenerator.database.Info.Trait;

import java.util.List;

public class TraitResult {
    public List<Trait> traits;
    public String traitType;

    public TraitResult(List<Trait> traits, String traitType) {
        this.traits = traits;
        this.traitType = traitType;
    }
}
