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

import dev.tim9h.rcpandroid.model.Track;
import dev.tim9h.rcpandroid.preferences.PrefsHelper;
import dev.tim9h.rcpandroid.service.client.RcpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
//        nowPlaying();
    }

    public LiveData<Intent> getOpenBrowserIntent() {
        return openBrowserIntent;
    }

    public void resetOpenBrowserIntent() {
        this.openBrowserIntent.setValue(null);
    }

    public void play() {
        isLoading.setValue(true);
        var call = RcpClient.getInstance().play();
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
        var call = RcpClient.getInstance().next();
        call.enqueue(createCallback());
    }

    public void previous() {
        isLoading.setValue(true);
        var call = RcpClient.getInstance().previous();
        call.enqueue(createCallback());
    }

    public void stop() {
        isLoading.setValue(true);
        var call = RcpClient.getInstance().stop();
        call.enqueue(createCallback());
    }

    public void nowPlaying() {
        isLoading.setValue(true);
        var call = RcpClient.getInstance().nowPlaying();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                isLoading.setValue(false);
                var t = response.body();
                track.postValue(t);
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("RCP", "Error while calling API", t);
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