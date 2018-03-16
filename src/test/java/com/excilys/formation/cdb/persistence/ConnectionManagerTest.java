package com.excilys.formation.cdb.persistence;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class ConnectionManagerTest {

	@Test
	public void getConnectionTest() {
		try (Connection conn = ConnectionManager.INSTANCE.getConnection()) {
			assertNotNull(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
