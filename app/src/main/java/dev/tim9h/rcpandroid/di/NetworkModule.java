// D:/misc/dev/android/rcp.companion/app/src/main/java/dev/tim9h/rcpandroid/di/NetworkModule.java
package dev.tim9h.rcpandroid.di;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dev.tim9h.rcpandroid.backend.api.LastFmApi;
import dev.tim9h.rcpandroid.backend.api.RcpApi;
import dev.tim9h.rcpandroid.backend.client.LastFmClient;
import dev.tim9h.rcpandroid.backend.client.RcpClient;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public RcpClient provideRcpClient(SharedPreferences preferences) { // Hilt gets SharedPreferences from AppModule
        return new RcpClient(preferences);
    }

    @Provides
    @Singleton
    public RcpApi provideRcpApi(RcpClient rcpClient) {
        return rcpClient.getApi();
    }

    @Provides
    @Singleton
    public LastFmClient provideLastFmClient() {
        return new LastFmClient();
    }

    @Provides
    @Singleton
    public LastFmApi provideLastFmApi(LastFmClient lastFmClient) {
        return lastFmClient.getApi();
    }

}
