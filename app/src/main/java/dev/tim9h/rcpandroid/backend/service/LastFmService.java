package dev.tim9h.rcpandroid.backend.service;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import dev.tim9h.rcpandroid.App;
import dev.tim9h.rcpandroid.backend.client.LastFmClient;
import dev.tim9h.rcpandroid.model.lastfm.TrackInfoResponse;
import retrofit2.Call;

public class LastFmService {

    private String apiKey;

    private static SharedPreferences.OnSharedPreferenceChangeListener changeListener;

    private static SharedPreferences preferences;

    private final LastFmClient client;

    public LastFmService() {
        preferences = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        changeListener = (_, key) -> {
            if (key != null && key.startsWith("rest_")) {
                Log.i("RCP", "Settings changed");
                apiKey = preferences.getString("lastfm_apikey", "");
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(changeListener);
        client = new LastFmClient();
    }

    private String getApiKey() {
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = preferences.getString("lastfm_apikey", "");
            if (apiKey.isEmpty()) {
                var msg = "Last.fm API key is not set in preferences";
                Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_SHORT).show();
                Log.w("RCP", msg);
            }
        }
        return apiKey;
    }

    public Call<TrackInfoResponse> getTrackInfo(String artist, String track) {
        return client.getApi().getTrackInfo("track.getInfo", getApiKey(), artist, track, "json");
    }

    public static void unregisterPreferenceChangeListener() {
        if (preferences != null) {
            preferences.unregisterOnSharedPreferenceChangeListener(changeListener);
            Log.d("RCP", "Preference change listener unregistered");
        }
    }

}
