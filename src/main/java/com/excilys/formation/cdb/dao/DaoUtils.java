package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoUtils {

    private static final String SQL_ERROR = "SQL error";
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoUtils.class);

    private DaoUtils() {}

    public static void closeElements(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
                LOGGER.debug("Closed ResultSet {}", resultSet);
            } catch (SQLException e) {
                LOGGER.error(SQL_ERROR, e);
            }
        }

        if (statement != null) {
            try {
                statement.close();
                LOGGER.debug("Closed Statement {}", statement);
            } catch (SQLException e) {
                LOGGER.error(SQL_ERROR, e);
            }
        }

        if (connection != null) {
            try {
                connection.close();
                LOGGER.debug("Closed Connection {}", connection);
            } catch (SQLException e) {
                LOGGER.error(SQL_ERROR, e);
            }
        }
    }

    public static void closeConnection(Connection connection) {
        closeElements(connection, null, null);
    }

}
