package dev.tim9h.rcpandroid.backend.service;

import dev.tim9h.rcpandroid.backend.client.RcpClient;
import dev.tim9h.rcpandroid.model.Track;
import retrofit2.Call;

public class RcpService {

    private final RcpClient client;

    public RcpService() {
        client = new RcpClient();
    }

    public Call<Void> next() {
        return client.getApi().next();
    }

    public Call<Void> play() {
        return client.getApi().play();
    }

    public Call<Void> previous() {
        return client.getApi().previous();
    }

    public Call<Void> stop() {
        return client.getApi().stop();
    }

    public Call<Void> mute() {
        return client.getApi().mute();
    }

    public Call<Void> volumeUp() {
        return client.getApi().volumeUp();
    }

    public Call<Void> volumeDown() {
        return client.getApi().volumeDown();
    }

    public Call<Track> nowPlaying() {
        return client.getApi().nowPlaying();
    }

}
