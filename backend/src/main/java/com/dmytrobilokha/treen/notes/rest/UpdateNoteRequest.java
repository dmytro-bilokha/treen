package com.dmytrobilokha.treen.notes.rest;

import javax.annotation.CheckForNull;
import java.util.List;

public class UpdateNoteRequest {

    private long id;
    @CheckForNull
    private Long parentId;
    @CheckForNull
    private String title;
    @CheckForNull
    private String link;
    @CheckForNull
    private List<NoteFlag> flags;
    @CheckForNull
    private String description;
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
    public List<NoteFlag> getFlags() {
        return flags;
    }

    public void setFlags(@CheckForNull List<NoteFlag> flags) {
        this.flags = flags;
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
