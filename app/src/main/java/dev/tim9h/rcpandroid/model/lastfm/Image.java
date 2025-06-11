package dev.tim9h.rcpandroid.model.lastfm;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("#text")
    private String text; // The actual image URL
    @SerializedName("size")
    private String size; // e.g., "small", "medium", "large", "extralarge"

    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}