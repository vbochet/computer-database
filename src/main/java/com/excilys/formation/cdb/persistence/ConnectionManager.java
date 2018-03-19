package com.excilys.formation.cdb.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    private Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
    
    private String CONFIG_FILE = "config/db/db.properties";
	
	private Properties props;
	private FileInputStream file;
	private String url;
	private String username;
	private String password;
	
	private Connection conn;
	
	private ConnectionManager() {
		props = new Properties();
		LOGGER.info("Loading DB configuration from file "+CONFIG_FILE);
		
		try {
			file = new FileInputStream(CONFIG_FILE);
		} catch (FileNotFoundException e) {
			LOGGER.error("Error: couldn't find file "+CONFIG_FILE);
			LOGGER.error(e.getLocalizedMessage());
		}
		
		try {
			props.load(file);
		} catch (IOException e) {
			LOGGER.error("Open/Read error on file "+CONFIG_FILE);
			LOGGER.error(e.getLocalizedMessage());
		}
		
		try {
			file.close();
		} catch (IOException e) {
			LOGGER.error("Close error on file "+CONFIG_FILE);
			LOGGER.error(e.getLocalizedMessage());
		}

		url = props.getProperty("jdbc.url");
		username = props.getProperty("jdbc.username");
		password = props.getProperty("jdbc.password");
	}

	public Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
			LOGGER.info("New connection created to DB "+url);
		} catch (SQLException e) {
			LOGGER.error("SQL error");
			LOGGER.error(e.getLocalizedMessage());
		}
		
		return conn;
	}

	
	public void closeElements(Connection conn, Statement st, ResultSet rs) {
		if(st != null) {
			try {
				st.close();
				LOGGER.info("Closed Statement "+st);
			} catch (SQLException e) {
				LOGGER.error("SQL error");
				LOGGER.error(e.getLocalizedMessage());
			}
		}
		
		if(conn != null) {
			try {
				conn.close();
				LOGGER.info("Closed Connection "+conn);
			} catch (SQLException e) {
				LOGGER.error("SQL error");
				LOGGER.error(e.getLocalizedMessage());
			}
		}
		
		if(rs != null) {
			try {
				rs.close();
				LOGGER.info("Closed ResultSet "+rs);
			} catch (SQLException e) {
				LOGGER.error("SQL error");
				LOGGER.error(e.getLocalizedMessage());
			}
		}
	}


}
