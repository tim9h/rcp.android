package dev.tim9h.rcpandroid.service.api;

import dev.tim9h.rcpandroid.model.Track;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RcpApi {

    @POST("play")
    Call<Void> play();

    @POST("next")
    Call<Void> next();

    @POST("previous")
    Call<Void> previous();

    @POST("stop")
    Call<Void> stop();

    @POST("lock")
    Call<Void> lock();

    @POST("shutdown")
    Call<Void> shutdown(@Field("time") String time);

    @GET("np")
    Call<Track> nowPlaying();

}
