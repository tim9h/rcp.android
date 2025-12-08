package dev.tim9h.rcpandroid.backend.client;

import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import dev.tim9h.rcpandroid.backend.api.RcpApi;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class RcpClient {

    private RcpApi api;
    private final SharedPreferences preferences;
    private final SharedPreferences.OnSharedPreferenceChangeListener changeListener;
    private Authenticator auth;
    private String baseUrl;

    @Inject
    public RcpClient(SharedPreferences preferences) {
        this.preferences = preferences;
        changeListener = (_, key) -> {
            if (key != null && key.startsWith("rest_")) {
                Log.i("RCP", "Settings changed");
                applyChangedPreferences();
            }
        };
        this.preferences.registerOnSharedPreferenceChangeListener(changeListener);
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
        if (baseUrl == null || baseUrl.isEmpty()) {
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

    public void unregisterPreferenceChangeListener() {
        if (preferences != null) {
            preferences.unregisterOnSharedPreferenceChangeListener(changeListener);
            Log.d("RCP", "Preference change listener unregistered");
        }
    }
}
