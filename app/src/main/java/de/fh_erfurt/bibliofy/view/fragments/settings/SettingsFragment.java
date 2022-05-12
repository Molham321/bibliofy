package de.fh_erfurt.bibliofy.view.fragments.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;

import de.fh_erfurt.bibliofy.R;
import de.fh_erfurt.bibliofy.view.fragments.core.Constants;

/**
 * setting View implementation class
 * is used to enable/disable dark mode
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        findPreference(Constants.PREF_DARK_MODE).setOnPreferenceChangeListener((preference, newValue) -> {
            AppCompatDelegate.setDefaultNightMode(
                    (Boolean) newValue ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );

            return true;
        });
    }
}
