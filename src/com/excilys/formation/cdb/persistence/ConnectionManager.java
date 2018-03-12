package com.excilys.formation.cdb.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public enum ConnectionManager {

	INSTANCE;
	
	private Properties props;
	private FileInputStream file;
	private String driver;
	private String url;
	private String username;
	private String password;
	
	private Connection conn;
	
	private ConnectionManager() {
		props = new Properties();
		
		try {
			file = new FileInputStream("config/db/db.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			props.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		driver = props.getProperty("jdbc.driver");
		if (driver != null) {
		    try {
				Class.forName(driver) ;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		url = props.getProperty("jdbc.url");
		username = props.getProperty("jdbc.username");
		password = props.getProperty("jdbc.password");

		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return conn;
	}

}
