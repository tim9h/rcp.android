package dev.tim9h.rcpandroid.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PrefsHelper {

    private final SharedPreferences preferences;

    public PrefsHelper(Context ctx) {
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
    }

    public void saveString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

}
