package dev.tim9h.rcpandroid.ui.media;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import dev.tim9h.rcpandroid.backend.service.LastFmService;
import dev.tim9h.rcpandroid.backend.service.RcpService;
import dev.tim9h.rcpandroid.model.Track;
import dev.tim9h.rcpandroid.model.lastfm.TrackInfoResponse;
import dev.tim9h.rcpandroid.preferences.PrefsHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaViewModel extends ViewModel {

    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final MutableLiveData<Track> track = new MutableLiveData<>();

    private final MutableLiveData<TrackInfoResponse> trackInfo = new MutableLiveData<>();

    private final MutableLiveData<Intent> openBrowserIntent = new MutableLiveData<>();

    private static PrefsHelper preferences;

    private final RcpService rcpService;

    private final LastFmService lastFmService;

    public MediaViewModel(PrefsHelper preferences) {
        MediaViewModel.preferences = preferences;
        lastFmService = new LastFmService();
        rcpService = new RcpService();
    }

    public LiveData<Track> getTrack() {
        return track;
    }

    public LiveData<TrackInfoResponse> getTrackInfo() {
        return trackInfo;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Intent> getOpenBrowserIntent() {
        return openBrowserIntent;
    }

    public void resetOpenBrowserIntent() {
        this.openBrowserIntent.setValue(null);
    }

    public void play() {
        isLoading.setValue(true);
        var call = rcpService.play();
        call.enqueue(createCallback());
    }

    public <T> Callback<T> createCallback() {
        return new Callback<>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("RCP", "Error while calling API", t);
                error.postValue(t.getMessage());
            }
        };
    }

    public void next() {
        isLoading.setValue(true);
        var call = rcpService.next();
        call.enqueue(createCallback());
    }

    public void previous() {
        isLoading.setValue(true);
        var call = rcpService.previous();
        call.enqueue(createCallback());
    }

    public void stop() {
        isLoading.setValue(true);
        var call = rcpService.stop();
        call.enqueue(createCallback());
    }

    public void nowPlaying() {
        isLoading.setValue(true);
        try {
            var call = rcpService.nowPlaying();
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    isLoading.setValue(false);
                    var t = response.body();
                    var trackChanged = track.getValue() == null || !track.getValue().equals(t);
                    track.postValue(t);
                    if (trackChanged && t != null) {
                        trackInfo(t.artist(), t.title());
                    }
                }

                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    isLoading.setValue(false);
                    Log.e("RCP", "Error while calling API", t);
                    error.postValue(t.getMessage());
                }
            });
        } catch (Throwable t) {
            Log.e("RCP", "Error while calling API", t);
        }
    }

    private void trackInfo(String artist, String title) {
        isLoading.setValue(true);
        var call = lastFmService.getTrackInfo(artist, title);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TrackInfoResponse> call, Response<TrackInfoResponse> response) {
                trackInfo.postValue(response.body());
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<TrackInfoResponse> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("RCP", "Error while calling Last.fm API", t);
                error.postValue(t.getMessage());
            }
        });
    }

    public void openLastFmTrack() {
        if (track.getValue() != null) {
            openUrl("https://www.last.fm/music/%s/_/%s", track.getValue().artist(), track.getValue().title());
        }
    }

    public void openLastFmArtist() {
        if (track.getValue() != null) {
            openUrl("https://www.last.fm/music/%s", track.getValue().artist());
        }
    }

    public void openLastFmAlbum() {
        if (track.getValue() != null) {
            openUrl("https://www.last.fm/music/%s/%s/", track.getValue().artist(), track.getValue().album());
        }
    }

    public void openLastFmProfile() {
        var username = preferences.getString("lastfm_username", "");
        if (!"".equals(username)) {
            openUrl("https://www.last.fm/user/%s", username);
        } else {
            openUrl("https://www.last.fm/");
        }
    }

    private void openUrl(String address, String... params) {
        var encodedParams = Arrays.stream(params).map(param -> URLEncoder.encode(param, StandardCharsets.UTF_8)).toArray();
        var url = String.format(address, encodedParams);
        var intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        openBrowserIntent.setValue(intent);
    }

}