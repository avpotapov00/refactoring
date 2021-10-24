package ru.akirakozov.sd.refactoring.repository.impl;

import lombok.SneakyThrows;
import ru.akirakozov.sd.refactoring.datasource.DataSource;
import ru.akirakozov.sd.refactoring.exception.DataSourceException;
import ru.akirakozov.sd.refactoring.repository.PreparedStatementConfigurer;
import ru.akirakozov.sd.refactoring.repository.ResultSetMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public abstract class BaseRepository {

    private final DataSource dataSource;

    protected BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    protected void save(String sql, PreparedStatementConfigurer configurer) {
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(sql);
            configurer.configure(statement);

            statement.execute();
        } catch (Exception e) {
            throw new DataSourceException(e);
        }
    }

    protected <T> T query(String sql, ResultSetMapper<T> mapper) {
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement(sql);
            var resultSet = statement.executeQuery();

            return mapper.map(resultSet);
        } catch (Exception e) {
            throw new DataSourceException(e);
        }
    }

    @SneakyThrows
    protected void createTable(String sql) {
        try (var connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

}
