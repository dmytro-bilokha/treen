package com.dmytrobilokha.treen.notes.persistence;

import com.dmytrobilokha.treen.db.UpsertQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateNoteQuery implements UpsertQuery {

    private static final String QUERY = "UPDATE note n"
            + " INNER JOIN user_data ud ON ud.id=n.user_id"
            + " SET n.title=?, n.link=?, n.description=?, ud.notebook_version=ud.notebook_version+1"
            + " WHERE n.id=? AND n.user_id=? AND ud.notebook_version=?";

    private final Note note;

    UpdateNoteQuery(Note note) {
        this.note = note;
    }

    @Override
    public String getQueryString() {
        return QUERY;
    }

    @Override
    public void setParameters(PreparedStatement statement) throws SQLException {
        statement.setString(1, note.getTitle());
        statement.setString(2, note.getLink());
        statement.setString(3, note.getDescription());
        statement.setLong(4, note.getId());
        statement.setLong(5, note.getUserId());
        statement.setLong(6, note.getVersion());
    }

}
