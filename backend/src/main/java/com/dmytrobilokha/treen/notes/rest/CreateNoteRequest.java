package com.dmytrobilokha.treen.notes.rest;

import javax.annotation.CheckForNull;

public class CreateNoteRequest {

    @CheckForNull
    private Long parentId;
    @CheckForNull
    private String title;
    @CheckForNull
    private String link;
    @CheckForNull
    private String description;
    private long version;

    @CheckForNull
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(@CheckForNull Long parentId) {
        this.parentId = parentId;
    }

    @CheckForNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@CheckForNull String title) {
        this.title = title;
    }

    @CheckForNull
    public String getLink() {
        return link;
    }

    public void setLink(@CheckForNull String link) {
        this.link = link;
    }

    @CheckForNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@CheckForNull String description) {
        this.description = description;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
