package com.nutlabs.keonics.dlna;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.nutlabs.keonics.R;

public class SettingsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.layout.activity_settings);
        }
    }
}
