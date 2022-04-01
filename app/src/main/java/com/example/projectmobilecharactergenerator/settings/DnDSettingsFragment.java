package com.example.projectmobilecharactergenerator.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.projectmobilecharactergenerator.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DnDSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.dnd_preferences, rootKey);

        MultiSelectListPreference backgroundPreference = findPreference("dnd_backgrounds");
        backgroundPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                MultiSelectListPreference listPreference = (MultiSelectListPreference) (preference);
                Set<String> newSelected = (Set<String>) newValue;
                if (newSelected.contains(getResources().getStringArray(R.array.background_array)[0]))
                    listPreference.setValues(new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.background_array))));
                else
                    listPreference.setValues(newSelected);
                return false;
            }
        });
    }

}