package ru.practicum.shareit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class IdReducer {

    private final DataSource dataSource;

    private static final String RESET_SQL_TEMPLATE = "ALTER TABLE %s ALTER COLUMN id RESTART WITH 1";

    public void resetAutoIncrementColumns(String tableName) throws SQLException {
        try (Connection dbConnection = dataSource.getConnection()) {
            try (Statement statement = dbConnection.createStatement()) {
                String resetSql = String.format(RESET_SQL_TEMPLATE, tableName);
                statement.execute(resetSql);
            }
        }
    }
}
