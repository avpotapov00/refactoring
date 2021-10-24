package ru.akirakozov.sd.refactoring.repository.impl;

import ru.akirakozov.sd.refactoring.config.DataSource;
import ru.akirakozov.sd.refactoring.exception.DataSourceException;
import ru.akirakozov.sd.refactoring.repository.PreparedStatementConfigurer;
import ru.akirakozov.sd.refactoring.repository.ResultSetMapper;

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

}
