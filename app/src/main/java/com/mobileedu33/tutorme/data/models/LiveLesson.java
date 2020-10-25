package com.mobileedu33.tutorme.data.models;

import java.time.LocalDateTime;

public class LiveLesson {
    private String id;
    private String title;
    private String description;
    private LocalDateTime datePosted;
    private LocalDateTime dateScheduled;
    private String imageUrl;
    private String link;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }

    public LocalDateTime getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(LocalDateTime dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
