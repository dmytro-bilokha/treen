package com.dmytrobilokha.treen.notes;

import javax.annotation.CheckForNull;

public class NoteNodeEntity {

    private final long id;
    @CheckForNull
    private final Long parentId;
    private final long userId;
    @CheckForNull
    private final String title;
    @CheckForNull
    private final String link;
    @CheckForNull
    private final String description;
    private final long version;

    public NoteNodeEntity(
            long id,
            @CheckForNull Long parentId,
            long userId,
            @CheckForNull String title,
            @CheckForNull String link,
            @CheckForNull String description,
            long version) {
        this.id = id;
        this.parentId = parentId;
        this.userId = userId;
        this.title = title;
        this.link = link;
        this.description = description;
        this.version = version;
    }

    public long getId() {
        return id;
    }

    @CheckForNull
    public Long getParentId() {
        return parentId;
    }

    public long getUserId() {
        return userId;
    }

    @CheckForNull
    public String getTitle() {
        return title;
    }

    @CheckForNull
    public String getLink() {
        return link;
    }

    @CheckForNull
    public String getDescription() {
        return description;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "NoteNodeEntity{"
                + "id=" + id
                + ", parentId=" + parentId
                + ", userId=" + userId
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", version=" + version
                + '}';
    }

}
