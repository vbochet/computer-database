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

public enum ConnectionManager {

	INSTANCE;
	
	private Properties props;
	private FileInputStream file;
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

		url = props.getProperty("jdbc.url");
		username = props.getProperty("jdbc.username");
		password = props.getProperty("jdbc.password");
	}

	public Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}

	
	public void closeElements(Connection conn, Statement st, ResultSet rs) {
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


}
