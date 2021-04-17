package com.dmytrobilokha.treen.notes;

import javax.annotation.CheckForNull;
import java.util.Comparator;
import java.util.List;

public class NoteNodeDto implements Comparable<NoteNodeDto> {

    private long id;
    @CheckForNull
    private String title;
    @CheckForNull
    private String link;
    @CheckForNull
    private String description;
    private long version;
    @CheckForNull
    private List<NoteNodeDto> children;

    @Override
    public int compareTo(NoteNodeDto o) {
        int baseResult = getComparisonBase().compareTo(o.getComparisonBase());
        if (baseResult != 0) {
            return baseResult;
        }
        return Long.compare(id, o.id);
    }

    private String getComparisonBase() {
        if (title != null) {
            return title;
        }
        if (link != null) {
            return link;
        }
        if (description != null) {
            return description;
        }
        //This should never happen
        return "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @CheckForNull
    public List<NoteNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<NoteNodeDto> children) {
        this.children = children;
    }

}
