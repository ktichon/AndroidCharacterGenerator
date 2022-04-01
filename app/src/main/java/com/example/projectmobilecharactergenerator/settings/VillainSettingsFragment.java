package com.example.projectmobilecharactergenerator.settings;

import android.os.Bundle;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.projectmobilecharactergenerator.R;

public class VillainSettingsFragment extends PreferenceFragmentCompat {

    MultiSelectListPreference villainTraitList;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.villain_preferences, rootKey);

    }
}