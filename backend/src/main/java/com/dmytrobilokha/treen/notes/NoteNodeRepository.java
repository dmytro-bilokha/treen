package com.dmytrobilokha.treen.notes;

import com.dmytrobilokha.treen.db.DbException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NoteNodeRepository {

    private DataSource dataSource;

    public NoteNodeRepository() {
        //CDI
    }

    @Inject
    public NoteNodeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<NoteNodeEntity> getUserNotes(long notesOwnerId) throws DbException {
        try(var connection = dataSource.getConnection();
            var statement = connection.prepareStatement(
                    "SELECT id, parent_id, user_id, title, link, description, version FROM note WHERE user_id=?")) {
            statement.setLong(1, notesOwnerId);
            try (var resultSet = statement.executeQuery()) {
                var result = new ArrayList<NoteNodeEntity>();
                while (resultSet.next()) {
                    var id = resultSet.getLong("id");
                    var parentId = resultSet.getObject("parent_id", Long.class);
                    var userId = resultSet.getLong("user_id");
                    var title = resultSet.getString("title");
                    var link = resultSet.getString("link");
                    var description = resultSet.getString("description");
                    var version = resultSet.getLong("version");
                    result.add(new NoteNodeEntity(id, parentId, userId, title, link, description, version));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DbException("Failure while trying to fetch notes for userId=" + notesOwnerId, e);
        }
    }

}
