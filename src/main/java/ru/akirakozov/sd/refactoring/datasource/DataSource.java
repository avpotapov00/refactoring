package ru.akirakozov.sd.refactoring.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import java.sql.Connection;

public class DataSource {

    private final HikariDataSource ds;

    public DataSource(String url) {
        var config = new HikariConfig();

        config.setJdbcUrl(url);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    @SneakyThrows
    public Connection getConnection() {
        return ds.getConnection();
    }
}