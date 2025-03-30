package dev.tim9h.rcpandroid;

import android.app.Application;

import dev.tim9h.rcpandroid.service.RetrofitClient;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RetrofitClient.unregisterPreferenceChangeListener();
    }

}
