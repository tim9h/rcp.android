package dev.tim9h.rcpandroid.ui.media;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import dev.tim9h.rcpandroid.model.Track;
import dev.tim9h.rcpandroid.preferences.PrefsHelper;
import dev.tim9h.rcpandroid.service.RetrofitClient;

public class MediaViewModel extends ViewModel {

    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final MutableLiveData<Track> track = new MutableLiveData<>();

    public LiveData<Track> getTrack() {
        return track;
    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    private final MutableLiveData<Intent> openBrowserIntent = new MutableLiveData<>();

    private static PrefsHelper preferences;

    public MediaViewModel(PrefsHelper preferences) {
        MediaViewModel.preferences = preferences;
        nowPlaying();
    }

    public LiveData<Intent> getOpenBrowserIntent() {
        return openBrowserIntent;
    }

    public void resetOpenBrowserIntent() {
        this.openBrowserIntent.setValue(null);
    }

    public <T> void processResponse(ListenableFuture<T> future) {
        processResponse(future, null);
    }

    public <T> void processResponse(ListenableFuture<T> future, MutableLiveData<T> data) {
        isLoading.postValue(true);
        Futures.addCallback(future, new FutureCallback<>() {
            @Override
            public void onSuccess(T result) {
                isLoading.postValue(false);
                if (data != null) {
                    data.postValue(result);
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                isLoading.postValue(false);
                error.postValue(t.getMessage() != null ? t.getMessage() : "An error occurred");
            }
        }, RetrofitClient.getExecutorService());
    }

    public void play() {
        isLoading.setValue(true);
        var future = RetrofitClient.getInstance().play();
        processResponse(future);
    }

    public void next() {
        isLoading.setValue(true);
        var future = RetrofitClient.getInstance().next();
        processResponse(future);
    }

    public void previous() {
        isLoading.setValue(true);
        var future = RetrofitClient.getInstance().previous();
        processResponse(future);
    }

    public void stop() {
        isLoading.setValue(true);
        var future = RetrofitClient.getInstance().stop();
        processResponse(future);
    }

    public void nowPlaying() {
        isLoading.setValue(true);
        var future = RetrofitClient.getInstance().nowPlaying();
        processResponse(future, track);
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