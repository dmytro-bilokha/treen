package com.dmytrobilokha.treen.notes.rest;

import javax.annotation.CheckForNull;

public class MoveNoteRequest {

    private long id;
    @CheckForNull
    private Long parentId;
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
