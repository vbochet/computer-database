package com.excilys.formation.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ConnectionManager {

    INSTANCE;

    private final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private String CONFIG_FILE = "db.properties";

    private Properties properties;
    private InputStream file;
    private String driver;
    private String url;
    private String username;
    private String password;

    private Connection connection;

    ConnectionManager() {
        properties = new Properties();
        LOGGER.info("Loading DB configuration from file " + CONFIG_FILE);

        file = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);

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
    }

    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            LOGGER.info("New connection created to DB " + url);
        } catch (SQLException e) {
            LOGGER.error("SQL error", e);
        }

        return connection;
    }


    public void closeElements(Connection connection, Statement statement, ResultSet resultSet) {
        if (statement != null) {
            try {
                statement.close();
                LOGGER.info("Closed Statement " + statement);
            } catch (SQLException e) {
                LOGGER.error("SQL error", e);
            }
        }

        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Closed Connection " + connection);
            } catch (SQLException e) {
                LOGGER.error("SQL error", e);
            }
        }

        if (resultSet != null) {
            try {
                resultSet.close();
                LOGGER.info("Closed ResultSet " + resultSet);
            } catch (SQLException e) {
                LOGGER.error("SQL error", e);
            }
        }
    }
}
