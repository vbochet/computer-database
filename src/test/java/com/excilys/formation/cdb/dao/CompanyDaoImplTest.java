package com.excilys.formation.cdb.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.*;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.model.Company;

public class CompanyDaoImplTest {
    
    final static int NB_COMPANY_IN_DB = 25;

    @BeforeClass
    public static void init() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        initDatabase();
    }

    @AfterClass
    public static void destroy() throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.executeUpdate("DROP TABLE company");
            connection.commit();
        }
    }

    private static void initDatabase() throws SQLException {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.execute("CREATE TABLE company (id bigint not null, name varchar(255), constraint pk_company primary key (id));");
            connection.commit();
            for(int i = 0; i < NB_COMPANY_IN_DB; i++) {
                statement.executeUpdate("insert into company (id,name) values (  "+i+",'company "+i+"');");
            }
            connection.commit();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:computer-database-db", "admincdb", "qwerty1234");
    }

    @Test
    public void listTest() throws DaoException {
        assertEquals(10, CompanyDaoImpl.INSTANCE.list(0, 10).size());
        assertEquals(NB_COMPANY_IN_DB, CompanyDaoImpl.INSTANCE.list(0, NB_COMPANY_IN_DB).size());
        assertEquals(NB_COMPANY_IN_DB, CompanyDaoImpl.INSTANCE.list(0, 0).size());
    }

    @Test
    public void readTest() {
        Company companyExpected = null;
        Company companyRead = null;
        for(int i = 0; i < NB_COMPANY_IN_DB; i++) {
            companyExpected = new Company(i, "company " + i);
            companyRead = CompanyDaoImpl.INSTANCE.read(i).get();

            assertEquals(companyExpected.getId(), companyRead.getId());
            assertEquals(companyExpected.getName(), companyRead.getName());
        }
    }

    @Test
    public void findByNameTest() {
        String name;
        Company companyExpected;
        Company companyFound;
        for(int i = 0; i < NB_COMPANY_IN_DB; i++) {
            name = "company " + i;
            companyExpected = new Company(i, name);
            companyFound = CompanyDaoImpl.INSTANCE.findByName(name).get();

            assertEquals(companyExpected.getId(), companyFound.getId());
            assertEquals(companyExpected.getName(), companyFound.getName());
        }
    }

    @Test
    public void countTest() {
        assertEquals(NB_COMPANY_IN_DB, CompanyDaoImpl.INSTANCE.count());
    }

}
