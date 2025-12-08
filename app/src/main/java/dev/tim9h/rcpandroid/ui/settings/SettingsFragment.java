package dev.tim9h.rcpandroid.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dev.tim9h.rcpandroid.R;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat {

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        var menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner());
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        decorateSetting("rest_base_url", false);
        decorateSetting("rest_api_key", true);
        decorateSetting("lastfm_username", false);
        decorateSetting("lastfm_apikey", true);
    }

    private void decorateSetting(String preferenceKey, boolean isPassword) {
        EditTextPreference preference = findPreference(preferenceKey);
        if (preference != null) {
            if (isPassword) {
                preference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
                preference.setSummary("••••••••");
            } else {
                preference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_TEXT_VARIATION_URI));
                preference.setSummary(getStoredValue(preferenceKey));
                preference.setOnPreferenceChangeListener((pref, val) -> {
                    pref.setSummary(val.toString());
                    return true;
                });
            }
        }
    }

    private String getStoredValue(String key) {
        return sharedPreferences.getString(key, "Not set");
    }

}
