package movies.swapover.de.movies.settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import movies.swapover.de.movies.R;

/**
 * Created by mikulicv on 10.12.15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String KEY_PREF_SORT_ORDER = "prefSortOrder";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        findPreference(KEY_PREF_SORT_ORDER)
                .setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SettingsFragment.KEY_PREF_SORT_ORDER, getString(R.string.pref_sort_order_default)));
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setBackgroundColor(Color.WHITE);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_SORT_ORDER)) {
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(SettingsFragment.KEY_PREF_SORT_ORDER, ""));
        }
    }
}
