package com.example.projectmobilecharactergenerator.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.projectmobilecharactergenerator.R;

public class CharacterSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.character_preferences, rootKey);
    }
}