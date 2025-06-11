package dev.tim9h.rcpandroid.model.lastfm;

import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}