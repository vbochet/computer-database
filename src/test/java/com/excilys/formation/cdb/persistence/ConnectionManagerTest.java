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
    }

    @Test
    public void closeElementsTest() {
        Connection conn = ConnectionManager.INSTANCE.getConnection();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT 1 AS nb;");
        } catch (SQLException e) {
            assertTrue(false);
        }
        
        ConnectionManager.INSTANCE.closeElements(conn, st, rs);
        try {
            assertTrue(rs.isClosed());
            assertTrue(st.isClosed());
            assertTrue(conn.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
