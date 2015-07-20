package com.cellasoft.ifoundit.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.cellasoft.ifoundit.R;
import com.cellasoft.ifoundit.utils.UIUtils;

import static com.cellasoft.ifoundit.utils.LogUtils.makeLogTag;

/**
 * Created by netsysco on 22/07/2014.
 */
public class SettingsActivity extends SherlockPreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!UIUtils.hasHoneycomb()) {
            onCreatePreferenceActivity();
        } else {
            onCreatePreferenceFragment();
        }

    }

    /**
     * Wraps legacy {@link #onCreate(Bundle)} code for Android < 3 (i.e. API lvl
     * < 11).
     */
    @SuppressWarnings("deprecation")
    private void onCreatePreferenceActivity() {
        addPreferencesFromResource(R.xml.settings);
    }

    /**
     * Wraps {@link #onCreate(Bundle)} code for Android >= 3 (i.e. API lvl >=
     * 11).
     */
    @SuppressLint("NewApi")
    private void onCreatePreferenceFragment() {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment())
                .commit();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Preference pref = findPreferenceBykey(key);
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        }
    }

    @SuppressWarnings("deprecation")
    private Preference findPreferenceBykey(String key) {
        return findPreference(key);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MyPreferenceFragment extends PreferenceFragment {
        private static final String TAG = makeLogTag(MyPreferenceFragment.class);

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            PreferenceManager.setDefaultValues(getActivity(),
                    R.xml.settings, false);
            addPreferencesFromResource(R.xml.settings);
        }
    }
}
