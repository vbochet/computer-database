package com.excilys.formation.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public enum ConnectionManager {

    INSTANCE;

    private final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private String CONFIG_FILE = "db.properties";
    private String url;

    private HikariDataSource ds;
    
    private final String SQL_ERROR = "SQL error";

    ConnectionManager() {
        InputStream file = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
        Properties properties = new Properties();
        String driver, username, password;
        int maxPoolSize;
        HikariConfig config = new HikariConfig();

        LOGGER.debug("Loading DB configuration from file {}", CONFIG_FILE);

        try {
            properties.load(file);
        } catch (IOException e) {
            LOGGER.error("Open/Read error on file {}", CONFIG_FILE, e);
        }

        try {
            file.close();
        } catch (IOException e) {
            LOGGER.error("Close error on file {}", CONFIG_FILE, e);
        }

        driver = properties.getProperty("jdbc.driver");
        if (driver != null) {
            try {
                Class.forName(driver) ;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        url = properties.getProperty("jdbc.url");
        username = properties.getProperty("jdbc.username");
        password = properties.getProperty("jdbc.password");
        maxPoolSize = Integer.parseInt(properties.getProperty("jdbc.maxPoolSize"));
        
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maxPoolSize);
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = ds.getConnection();
            LOGGER.debug("New connection created to DB {}", url);
        } catch (SQLException e) {
            LOGGER.error(SQL_ERROR, e);
        }

        return connection;
    }


    public void closeElements(Connection connection, Statement statement, ResultSet resultSet) {
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
}
