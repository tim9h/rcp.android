package dev.tim9h.rcpandroid.backend.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import dev.tim9h.rcpandroid.backend.client.RcpClient;
import dev.tim9h.rcpandroid.model.LogiledStatus;
import dev.tim9h.rcpandroid.model.Track;
import retrofit2.Call;

@Singleton
public class RcpService {

    private final RcpClient client;

    @Inject
    public RcpService(RcpClient client) {
        this.client = client;
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

    public Call<Void> logiledOn() {
        return client.getApi().logiled("on");
    }

    public Call<Void> logiledOff() {
        return client.getApi().logiled("off");
    }

    public Call<LogiledStatus> logiledStatus() {
        return client.getApi().logiledStatus();
    }

    public Call<Void> logiled(String color) {
        return client.getApi().logiled(color);
    }

}
