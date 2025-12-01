package dev.tim9h.rcpandroid.backend.client;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import dev.tim9h.rcpandroid.App;
import dev.tim9h.rcpandroid.backend.api.RcpApi;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RcpClient {

    private RcpApi api;

    private static SharedPreferences preferences;

    private static SharedPreferences.OnSharedPreferenceChangeListener changeListener;

    private Authenticator auth;

    private String baseUrl;

    public RcpClient() {
        preferences = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        changeListener = (_, key) -> {
            if (key != null && key.startsWith("rest_")) {
                Log.i("RCP", "Settings changed");
                applyChangedPreferences();
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(changeListener);
        applyChangedPreferences();
    }

    private void applyChangedPreferences() {
        baseUrl = preferences.getString("rest_base_url", "");
        var apiKey = preferences.getString("rest_api_key", "");
        auth = (_, response) -> response.request().newBuilder().header("X-API-Key", apiKey).build();
        api = null;
        Log.d("RCP", "init: baseUrl: " + baseUrl);
    }

    public RcpApi getApi() {
        if (baseUrl == null) {
            return null;
        }
        if (api == null) {
            var loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            var client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).authenticator(auth).build();
            var retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            api = retrofit.create(RcpApi.class);
        }
        return api;
    }

    public static void unregisterPreferenceChangeListener() {
        if (preferences != null) {
            preferences.unregisterOnSharedPreferenceChangeListener(changeListener);
            Log.d("RCP", "Preference change listener unregistered");
        }
    }

}