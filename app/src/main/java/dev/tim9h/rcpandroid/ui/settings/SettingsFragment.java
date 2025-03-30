package dev.tim9h.rcpandroid.ui.settings;

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
import androidx.preference.PreferenceManager;

import dev.tim9h.rcpandroid.R;


public class SettingsFragment extends PreferenceFragmentCompat {

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

        var user = findPreference("rest_api_username");
        if (user != null) {
            user.setSummary(getStoredValue("rest_api_username", "Not set"));
            user.setOnPreferenceChangeListener((preference, newValue) -> {
                preference.setSummary(newValue.toString());
                return true;
            });
        }

        EditTextPreference password = findPreference("rest_api_password");
        if (password != null) {
            password.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
            password.setSummary(getStoredValue("rest_api_password", "").isEmpty() ? "Not set" : "••••••••");
            password.setOnPreferenceClickListener(preference -> {
                preference.setSummary("••••••••");
                return true;
            });
        }

        EditTextPreference port = findPreference("rest_base_url");
        if (port != null) {
            port.setSummary(getStoredValue("rest_base_url", "Not set"));
            port.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_TEXT_VARIATION_URI));
            port.setOnPreferenceChangeListener((preference, newValue) -> {
                preference.setSummary(newValue.toString());
                return true;
            });
        }
    }

    private String getStoredValue(String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getString(key, defaultValue);
    }

}
