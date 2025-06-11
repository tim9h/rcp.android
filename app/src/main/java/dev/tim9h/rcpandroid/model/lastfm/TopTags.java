package dev.tim9h.rcpandroid.model.lastfm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopTags {
    @SerializedName("tag")
    private List<Tag> tag;

    // Getters and Setters
    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }
}