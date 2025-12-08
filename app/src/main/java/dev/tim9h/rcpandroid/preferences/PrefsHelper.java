package dev.tim9h.rcpandroid.preferences;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrefsHelper {

    private final SharedPreferences preferences;

    @Inject
    public PrefsHelper(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

}
