package com.excilys.formation.cdb.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.junit.*;

import com.excilys.formation.cdb.exceptions.DaoException;

public class ComputerDaoImplTest {
    
    final static int NB_COMPUTER_IN_DB = 25;

    @BeforeClass
    public static void init() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        initDatabase();
    }

    @AfterClass
    public static void destroy() throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.executeUpdate("DROP TABLE company");
            statement.executeUpdate("DROP TABLE computer");
            connection.commit();
        }
    }

    private static void initDatabase() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.execute("CREATE TABLE company (id bigint not null, name varchar(255), constraint pk_company primary key (id));");
            statement.execute("CREATE TABLE computer (id bigint not null, name varchar(255), introduced datetime NULL, discontinued datetime NULL, company_id bigint default NULL, constraint pk_computer primary key (id));");
            connection.commit();
            for(int i = 0; i < NB_COMPUTER_IN_DB; i++) {
                statement.executeUpdate("insert into company (id,name) values (  "+i+",'company "+i+"');");
                statement.executeUpdate("insert into computer (id,name,company_id) values (  "+i+",'computer "+i+"', "+i+");");
            }
            connection.commit();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:computer-database-db", "admincdb", "qwerty1234");
    }

    @Test
    public void listTest() throws DaoException {
        assertEquals(10, ComputerDaoImpl.INSTANCE.list(0, 10).size());
        assertEquals(NB_COMPUTER_IN_DB, ComputerDaoImpl.INSTANCE.list(0, NB_COMPUTER_IN_DB).size());
        assertEquals(NB_COMPUTER_IN_DB, ComputerDaoImpl.INSTANCE.list(0, 0).size());
    }

}
