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

import dev.tim9h.rcpandroid.model.Track;
import dev.tim9h.rcpandroid.service.RetrofitClient;

public class MediaViewModel extends ViewModel {

    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final MutableLiveData<Track> track = new MutableLiveData<>();

    public MediaViewModel() {
        nowPlaying();
    }

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
            var url = String.format("https://www.last.fm/music/%s/_/%s", encodeValue(track.getValue().artist()), encodeValue(track.getValue().title()));
            var intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            openBrowserIntent.setValue(intent);
        }
    }

    public void openLastFmArtist() {
        if (track.getValue() != null) {
            var url = String.format("https://www.last.fm/music/%s", encodeValue(track.getValue().artist()));
            var intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            openBrowserIntent.setValue(intent);
        }
    }

    public void openLastFmAlbum() {
        if (track.getValue() != null) {
            var url = String.format("https://www.last.fm/music/%s/%s/", encodeValue(track.getValue().artist()), encodeValue(track.getValue().album()));
            var intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            openBrowserIntent.setValue(intent);
        }
    }

    private static String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}