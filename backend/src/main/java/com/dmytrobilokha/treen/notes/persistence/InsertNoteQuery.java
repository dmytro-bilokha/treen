package com.dmytrobilokha.treen.notes.persistence;

import com.dmytrobilokha.treen.db.UpsertQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertNoteQuery implements UpsertQuery {

    private static final String QUERY =
            "INSERT INTO note (parent_id, user_id, title, link, description)"
                    + " SELECT pn.id, pn.user_id, ?, ?, ?"
                    + " FROM note pn"
                    + " INNER JOIN user_data ud ON pn.user_id=ud.id"
                    + " WHERE pn.id=? AND pn.user_id=? AND ud.notebook_version=?";

    private final NewNote note;

    public InsertNoteQuery(NewNote note) {
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
        statement.setObject(4, note.getParentId());
        statement.setLong(5, note.getUserId());
        statement.setLong(6, note.getVersion());
    }

}
