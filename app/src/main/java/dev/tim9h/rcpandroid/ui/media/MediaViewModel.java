package dev.tim9h.rcpandroid.ui.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MediaViewModel extends ViewModel {

    private final MutableLiveData<String> title;

    private final MutableLiveData<String> artist;

    private final MutableLiveData<String> album;

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

}