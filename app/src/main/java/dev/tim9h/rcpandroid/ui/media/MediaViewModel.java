package dev.tim9h.rcpandroid.ui.media;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

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

}