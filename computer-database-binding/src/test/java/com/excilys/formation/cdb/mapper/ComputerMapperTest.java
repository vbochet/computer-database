package com.excilys.formation.cdb.mapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.formation.cdb.configuration.TestBindingConfig;
import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBindingConfig.class})
public class ComputerMapperTest {

    static int NB_COMPUTER_IN_DB = 25;
    
    @Autowired
    ComputerMapper computerMapper;

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
	public void createComputerTest() throws SQLException {
		Computer computer;
		
		final String NAME = "toto";
		final Date DATE_1 = new Date(0);
		final Date DATE_2 = new Date(1000);
		final Company COMPANY = new Company(3,"RCA");
		
		ResultSet rs = mock(ResultSet.class);
		
		/* Regular case with all fields filled */
		//given
		doReturn(NAME).when(rs).getString("name");
		doReturn(DATE_1).when(rs).getDate("introduced");
		doReturn(DATE_2).when(rs).getDate("discontinued");
		doReturn(COMPANY.getId()).when(rs).getLong("company_id");
		doReturn(COMPANY.getName()).when(rs).getString("company_name");
		
		//when
		computer = computerMapper.resultSetToComputer(rs);
		
		//then
		assertEquals(NAME, computer.getName());
		assertEquals(DATE_1.toLocalDate(), computer.getIntroduced());
		assertEquals(DATE_2.toLocalDate(), computer.getDiscontinued());
		assertEquals(COMPANY.getId(), computer.getCompany().getId());
		assertEquals(COMPANY.getName(), computer.getCompany().getName());
		
		
		/* Regular case with only name filled */
		// given
		doReturn(NAME).when(rs).getString("name");
		doReturn(null).when(rs).getDate("introduced");
		doReturn(null).when(rs).getDate("discontinued");
		doReturn(0L).when(rs).getLong("company_id");
		doReturn(null).when(rs).getString("company_name");
		
		//when
		computer = computerMapper.resultSetToComputer(rs);
		
		//then
		assertEquals(NAME, computer.getName());
		assertEquals(null, computer.getIntroduced());
		assertEquals(null, computer.getDiscontinued());
		assertEquals(0, computer.getCompany().getId());
		assertEquals(null, computer.getCompany().getName());
	}

    @Test
    public void computerToComputerDtoTest() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        //given
        final int ID = 42;
        final int NO_ID = 0;
        final String NAME = "toto";
        final LocalDate DATE_1 = LocalDate.parse("2000-01-01", formatter);
        final LocalDate DATE_2 = LocalDate.parse("2010-05-03", formatter);
        final int companyId = 3;
        final String companyName = "company 3";
        final Company COMPANY = new Company(companyId,companyName);
        
        Computer computer;
        ComputerDto computerDto;

        //when
        computer = new Computer(ID, NAME, DATE_1, DATE_2, COMPANY);
        computerDto = computerMapper.computerToComputerDto(computer);

        //then
        assertEquals(ID, computerDto.getComputerId());
        assertEquals(NAME, computerDto.getComputerName());
        assertEquals(DATE_1.toString(), computerDto.getComputerIntroduced().toString());
        assertEquals(DATE_2.toString(), computerDto.getComputerDiscontinued().toString());
        assertEquals(companyName, computerDto.getComputerCompanyName());


        //when
        computer = new Computer(NO_ID, null, null, null, null);
        computerDto = computerMapper.computerToComputerDto(computer);

        //then
        assertEquals(NO_ID, computerDto.getComputerId());
        assertEquals(null, computerDto.getComputerName());
        assertEquals(null, computerDto.getComputerIntroduced());
        assertEquals(null, computerDto.getComputerDiscontinued());
        assertEquals(null, computerDto.getComputerCompanyName());

    }

}
