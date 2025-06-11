package dev.tim9h.rcpandroid.model.lastfm;

import com.google.gson.annotations.SerializedName;

public class TrackInfoResponse {
    @SerializedName("track")
    private Track track;

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

}