package ru.akirakozov.sd.refactoring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementConfigurer {

    void configure(PreparedStatement preparedStatement) throws SQLException;

}
