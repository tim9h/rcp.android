package dev.tim9h.rcpandroid.backend.service;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

import dev.tim9h.rcpandroid.backend.client.LastFmClient;
import dev.tim9h.rcpandroid.model.lastfm.TrackInfoResponse;
import retrofit2.Call;

@Singleton
public class LastFmService {

    private String apiKey;

    private final SharedPreferences preferences;

    private final LastFmClient client;

    private final Application application;

    @Inject
    public LastFmService(LastFmClient client, SharedPreferences preferences, Application application) {
        this.client = client;
        this.preferences = preferences;
        this.application = application;
        this.preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if (key != null && key.equals("lastfm_apikey")) {
                Log.i("RCP", "Last.fm API key changed");
                this.apiKey = sharedPreferences.getString("lastfm_apikey", "");
            }
        });
    }

    private String getApiKey() {
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = preferences.getString("lastfm_apikey", "");
            if (apiKey.isEmpty()) {
                var msg = "Last.fm API key is not set in preferences";
                Toast.makeText(application, msg, Toast.LENGTH_SHORT).show();
                Log.w("RCP", msg);
            }
        }
        return apiKey;
    }

    public Call<TrackInfoResponse> getTrackInfo(String artist, String track) {
        return client.getApi().getTrackInfo("track.getInfo", getApiKey(), artist, track, "json");
    }

}
