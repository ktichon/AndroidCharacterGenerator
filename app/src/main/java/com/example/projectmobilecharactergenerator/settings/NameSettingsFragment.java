package com.example.projectmobilecharactergenerator.settings;

import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import com.example.projectmobilecharactergenerator.R;

public class NameSettingsFragment extends PreferenceFragmentCompat {
    SeekBarPreference syllablePref;
        SwitchPreference nameGeneratorUsed;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.name_preferences, rootKey);

        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());


        nameGeneratorUsed = (SwitchPreference) findPreference("name_generator_used");
        nameGeneratorUsed.setOnPreferenceChangeListener(onGeneratorUsedChange);
        currentGenerator(pref.getBoolean("name_generator_used", true));

        syllablePref = (SeekBarPreference) findPreference("max_syllable_length");
        syllablePref.setSummary("Max Amount of Syllables: " + syllablePref.getValue());
        syllablePref.setOnPreferenceChangeListener(onSyllableChange);
        ListPreference nameComplexity = (ListPreference) findPreference("name_complexity");

        if(nameComplexity.getValue() == null){
            nameComplexity.setValueIndex(0);
        }
    }
    Preference.OnPreferenceChangeListener onSyllableChange = (preference, newValue) -> {
        int value = Integer.valueOf(String.valueOf(newValue));
        syllablePref.setSummary("Max Amount of Syllables: " + value);
        return true;
    };


    Preference.OnPreferenceChangeListener onGeneratorUsedChange = (preference, newValue) -> {
        currentGenerator((Boolean)newValue);
        return true;

    };

    private void currentGenerator(Boolean genUsed)
    {
        PreferenceCategory custom = (PreferenceCategory) findPreference("custom_generator");
        PreferenceCategory web = (PreferenceCategory) findPreference("web_generator");
        if (genUsed)
        {
            custom.setVisible(true);
            web.setVisible(false);
        }
        else {
            custom.setVisible(false);
            web.setVisible(true);
        }
    }
}