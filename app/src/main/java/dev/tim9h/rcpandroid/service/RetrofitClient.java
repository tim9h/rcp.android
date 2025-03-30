package dev.tim9h.rcpandroid.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static ApiService apiService;

    private static Context applicationContext;

    private static SharedPreferences preferences;

    private static SharedPreferences.OnSharedPreferenceChangeListener changeListener;

    private static Authenticator auth;

    private static String baseUrl;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static void initialize(Context context) {
        if (applicationContext == null) {
            applicationContext = context.getApplicationContext();
            preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            changeListener = (_, key) -> {
                if (key != null && key.startsWith("rest_")) {
                    Log.i("RCP", "Settings changed");
                    initClient();
                }
            };
            preferences.registerOnSharedPreferenceChangeListener(changeListener);
            initClient();
        }
    }

    private static void initClient() {
        baseUrl = preferences.getString("rest_base_url", "");
        var username = preferences.getString("rest_api_username", "");
        var password = preferences.getString("rest_api_password", "");
        auth = (_, response) -> {
            String credential = Credentials.basic(username, password);
            return response.request().newBuilder().header("Authorization", credential).build();
        };
        apiService = null;
        Log.d("RCP", "init: baseUrl: " + baseUrl + " username: " + username + " password: ******");
    }

    public static ApiService getInstance() {
        if (apiService == null) {
            var loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            var client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).authenticator(auth).build();
            var retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static void unregisterPreferenceChangeListener() {
        if (preferences != null) {
            preferences.unregisterOnSharedPreferenceChangeListener(changeListener);
            Log.d("RCP", "Preference change listener unregistered");
        }
    }

}