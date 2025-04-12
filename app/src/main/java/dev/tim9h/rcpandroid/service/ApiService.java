package dev.tim9h.rcpandroid.service;

import com.google.common.util.concurrent.ListenableFuture;

import dev.tim9h.rcpandroid.model.Track;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("play")
    ListenableFuture<Void> play();

    @POST("next")
    ListenableFuture<Void> next();

    @POST("previous")
    ListenableFuture<Void> previous();

    @POST("stop")
    ListenableFuture<Void> stop();

    @POST("lock")
    ListenableFuture<Void> lock();

    @POST("shutdown")
    ListenableFuture<Void> shutdown(@Field("time") String time);


    @GET("np")
    ListenableFuture<Track> nowPlaying();

}
