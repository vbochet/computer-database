package com.excilys.formation.cdb.mapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;

public class ComputerMapperTest {

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
		computer = ComputerMapper.INSTANCE.resultSetToComputer(rs);
		
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
		computer = ComputerMapper.INSTANCE.resultSetToComputer(rs);
		
		//then
		assertEquals(NAME, computer.getName());
		assertEquals(null, computer.getIntroduced());
		assertEquals(null, computer.getDiscontinued());
		assertEquals(0, computer.getCompany().getId());
		assertEquals(null, computer.getCompany().getName());
	}

}
