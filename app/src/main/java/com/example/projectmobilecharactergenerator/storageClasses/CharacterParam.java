package com.example.projectmobilecharactergenerator.storageClasses;

import android.content.Context;

public class CharacterParam {
    public long id;
    public String name;
    public Context context;

    public CharacterParam(long id, String name, Context context) {
        this.id = id;
        this.name = name;
        this.context = context;
    }
}
