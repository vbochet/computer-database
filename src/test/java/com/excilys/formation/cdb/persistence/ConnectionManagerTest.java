package com.excilys.formation.cdb.persistence;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class ConnectionManagerTest {

    @Test
    public void getConnectionTest() {
        Connection conn = ConnectionManager.INSTANCE.getConnection();
        assertNotNull(conn);
        ConnectionManager.INSTANCE.closeElements(conn, null, null);
    }

    @Test
    public void closeElementsTest() throws SQLException {
        Connection conn = ConnectionManager.INSTANCE.getConnection();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT 1 AS nb;");
    
        ConnectionManager.INSTANCE.closeElements(conn, st, rs);
        assertTrue(rs.isClosed());
        assertTrue(st.isClosed());
        assertTrue(conn.isClosed());
    }

}
