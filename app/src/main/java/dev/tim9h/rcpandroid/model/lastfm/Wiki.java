package dev.tim9h.rcpandroid.model.lastfm;

import com.google.gson.annotations.SerializedName;

public class Wiki {
    @SerializedName("published")
    private String published;
    @SerializedName("summary")
    private String summary;
    @SerializedName("content")
    private String content;

    // Getters and Setters
    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}