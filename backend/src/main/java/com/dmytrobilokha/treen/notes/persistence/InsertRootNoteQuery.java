package com.dmytrobilokha.treen.notes.persistence;

import com.dmytrobilokha.treen.db.UpsertQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertRootNoteQuery implements UpsertQuery {

    private static final String QUERY =
            "INSERT INTO note (parent_id, user_id, title, link, description)"
                    + " SELECT NULL, ud.id, ?, ?, ?"
                    + " FROM user_data ud WHERE ud.id=? AND notebook_version=?";

    private final NewNote note;

    InsertRootNoteQuery(NewNote note) {
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
        statement.setLong(4, note.getUserId());
        statement.setLong(5, note.getVersion());
    }

}
