package dev.tim9h.rcpandroid;

import android.app.Application;
import android.content.Context;

import dev.tim9h.rcpandroid.backend.client.RcpClient;
import dev.tim9h.rcpandroid.backend.service.LastFmService;

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RcpClient.unregisterPreferenceChangeListener();
        LastFmService.unregisterPreferenceChangeListener();
    }

    public static Context getAppContext() {
        return App.context;
    }

}
