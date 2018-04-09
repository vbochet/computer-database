package com.excilys.formation.cdb.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.junit.*;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;

public class ComputerDaoImplTest {
    
    static int NB_COMPUTER_IN_DB = 25;

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

    @Test
    public void readTest() {
        Computer computerExpected = null;
        Computer computerRead = null;
        for(int i = 0; i < NB_COMPUTER_IN_DB; i++) {
            computerExpected = new Computer(i, "computer "+i, null, null, new Company(i, "company "+i));
            computerRead = ComputerDaoImpl.INSTANCE.read(i).get();

            assertEquals(computerExpected.getId(), computerRead.getId());
            assertEquals(computerExpected.getName(), computerRead.getName());
            assertEquals(computerExpected.getIntroduced(), computerRead.getIntroduced());
            assertEquals(computerExpected.getDiscontinued(), computerRead.getDiscontinued());
            assertEquals(computerExpected.getCompany().getId(), computerRead.getCompany().getId());
            assertEquals(computerExpected.getCompany().getName(), computerRead.getCompany().getName());
        }
    }

    @Test
    public void createTest() {
        Computer computer = new Computer(42, "computer 42", null, null, new Company(1, "company 1"));
        Computer computerCreated = ComputerDaoImpl.INSTANCE.create(computer);

        assertEquals(computer.getId(), computerCreated.getId());
        assertEquals(computer.getName(), computerCreated.getName());
        assertEquals(computer.getIntroduced(), computerCreated.getIntroduced());
        assertEquals(computer.getDiscontinued(), computerCreated.getDiscontinued());
        assertEquals(computer.getCompany().getId(), computerCreated.getCompany().getId());
        assertEquals(computer.getCompany().getName(), computerCreated.getCompany().getName());
        
        NB_COMPUTER_IN_DB++;
    }

    @Test
    public void updateTest() {
        Computer computer = new Computer(1, "computer 100", null, null, new Company(10, "company 10"));
        Computer computerUpdated = ComputerDaoImpl.INSTANCE.update(computer);

        assertEquals(computer.getId(), computerUpdated.getId());
        assertEquals(computer.getName(), computerUpdated.getName());
        assertEquals(computer.getIntroduced(), computerUpdated.getIntroduced());
        assertEquals(computer.getDiscontinued(), computerUpdated.getDiscontinued());
        assertEquals(computer.getCompany().getId(), computerUpdated.getCompany().getId());
        assertEquals(computer.getCompany().getName(), computerUpdated.getCompany().getName());

        computer = new Computer(1, "computer 1", null, null, new Company(1, "company 1"));
        ComputerDaoImpl.INSTANCE.update(computer);
    }

    @Test
    public void deleteTest() {
        ComputerDaoImpl.INSTANCE.delete(5);
        NB_COMPUTER_IN_DB++;
        
        Optional<Computer> oc = ComputerDaoImpl.INSTANCE.read(5);
        assertEquals(Optional.empty(), oc);
    }

    @Test
    public void countTest() {
        assertEquals(NB_COMPUTER_IN_DB, ComputerDaoImpl.INSTANCE.count());
    }

}
