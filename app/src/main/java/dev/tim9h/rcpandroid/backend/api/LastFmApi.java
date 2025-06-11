package dev.tim9h.rcpandroid.backend.api;

import dev.tim9h.rcpandroid.model.lastfm.TrackInfoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFmApi {

    @GET("2.0/")
    Call<TrackInfoResponse> getTrackInfo(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query(value = "artist", encoded = true) String artist,
            @Query(value = "track", encoded = true) String track,
            @Query("format") String format
    );

}
