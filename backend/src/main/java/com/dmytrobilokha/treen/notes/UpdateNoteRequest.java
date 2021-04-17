package com.dmytrobilokha.treen.notes;

import javax.annotation.CheckForNull;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.util.Objects;

//TODO: change this to be a regular bean, because jsonb throws 500 when some field is missing
public class UpdateNoteRequest {

    private final long id;
    @CheckForNull
    private final String title;
    @CheckForNull
    private final String link;
    @CheckForNull
    private final String description;
    private final long version;

    @JsonbCreator
    public UpdateNoteRequest(@CheckForNull @JsonbProperty("id") Long id,
                             @CheckForNull @JsonbProperty("title") String title,
                             @CheckForNull @JsonbProperty("link") String link,
                             @CheckForNull @JsonbProperty("description") String description,
                             @CheckForNull @JsonbProperty("version") Long version) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        if ((title == null || title.isBlank())
            && (link == null || link.isBlank())) {
            throw new IllegalArgumentException("either title or link should be provided");
        }
        this.title = title;
        this.link = link;
        this.description = description;
        this.version = Objects.requireNonNull(version, "version must not be null");
    }

    public long getId() {
        return id;
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
        return "UpdateNoteRequest{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", version=" + version
                + '}';
    }

}
