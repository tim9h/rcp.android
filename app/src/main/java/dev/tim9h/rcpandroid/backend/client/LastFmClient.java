package dev.tim9h.rcpandroid.backend.client;

import dev.tim9h.rcpandroid.backend.api.LastFmApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LastFmClient {

    private LastFmApi api;

    private static final String BASE_URL = "http://ws.audioscrobbler.com/";

    public LastFmApi getApi() {
        if (api == null) {
            var loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
            var client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
            var retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            api = retrofit.create(LastFmApi.class);
        }
        return api;
    }

}
