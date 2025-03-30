package dev.tim9h.rcpandroid.ui.media;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import dev.tim9h.rcpandroid.service.RetrofitClient;

public class MediaViewModel extends ViewModel {

    private final MutableLiveData<String> title;

    private final MutableLiveData<String> artist;

    private final MutableLiveData<String> album;

    private MutableLiveData<Void> data = new MutableLiveData<>();

    private MutableLiveData<String> error = new MutableLiveData<>();

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MediaViewModel() {
        title = new MutableLiveData<>();
        title.setValue("I am the title");

        artist = new MutableLiveData<>();
        artist.setValue("I am the artist");

        album = new MutableLiveData<>();
        album.setValue("I am the album");
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getArtist() {
        return artist;
    }

    public LiveData<String> getAlbum() {
        return album;
    }

    public MutableLiveData<Boolean> isLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public MutableLiveData<Void> getData() {
        return data;
    }

    public void processResponse(ListenableFuture<Void> future) {
        Futures.addCallback(future, new FutureCallback<>() {
            @Override
            public void onSuccess(Void result) {
                isLoading.postValue(false);
                data.postValue(result);
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

}