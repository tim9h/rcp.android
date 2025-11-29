package dev.tim9h.rcpandroid.backend.api;

import dev.tim9h.rcpandroid.model.Track;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RcpApi {

    @POST("play")
    Call<Void> play();

    @POST("next")
    Call<Void> next();

    @POST("previous")
    Call<Void> previous();

    @POST("stop")
    Call<Void> stop();

    @POST("mute")
    Call<Void> mute();

    @POST("volumeup")
    Call<Void> volumeUp();

    @POST("volumedown")
    Call<Void> volumeDown();

    @POST("lock")
    Call<Void> lock();

    @POST("shutdown")
    @FormUrlEncoded
    Call<Void> shutdown(@Field("time") String time);

    @GET("np")
    Call<Track> nowPlaying();

    @POST("logiled")
    Call<Void> logiled(@Query("color") String color);

    @GET("logiled")
    Call<Boolean> logiledStatus();

    @GET("logiledcolor")
    Call<String> logiledColor();

}
