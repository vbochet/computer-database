package com.excilys.formation.cdb.persistence;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Test;

public class ConnectionManagerTest {

	@Test
	public void getConnectionTest() {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		assertNotNull(conn);
	}

}
