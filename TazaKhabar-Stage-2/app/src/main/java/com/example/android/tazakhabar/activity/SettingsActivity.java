package com.example.android.tazakhabar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.tazakhabar.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    //Custom Preference Fragment for SettingActivity
    public static class NewsPreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //add custom preference resource
            addPreferencesFromResource(R.xml.settings_main);

            //bind summary value to the preferences
            Preference orderBy = findPreference(getString(R.string.order_by_key));
            bindPreferenceSummaryToValue(orderBy);
            Preference sectionName = findPreference(getString(R.string.section_news_key));
            bindPreferenceSummaryToValue(sectionName);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringObject = o.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int preferenceIndex = listPreference.findIndexOfValue(stringObject);

                if (preferenceIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[preferenceIndex]);
                }
            } else {
                preference.setSummary(stringObject);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            //set preference change listener to fetch the updated preference value
            preference.setOnPreferenceChangeListener(this);

            //get the stored values from SharedPreferences
            SharedPreferences sharedPreferences = PreferenceManager.
                    getDefaultSharedPreferences(preference.getContext());

            String preferenceString = sharedPreferences.getString(preference.getKey(), "");
            //invoke onPreferenceChange to update the summary with the new value
            onPreferenceChange(preference, preferenceString);
        }
    }
}
