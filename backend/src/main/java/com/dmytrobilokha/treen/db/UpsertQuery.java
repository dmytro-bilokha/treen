package com.dmytrobilokha.treen.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface UpsertQuery {

    String getQueryString();

    void setParameters(PreparedStatement statement) throws SQLException;

}
