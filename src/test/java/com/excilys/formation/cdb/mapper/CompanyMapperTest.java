package com.excilys.formation.cdb.mapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.excilys.formation.cdb.model.Company;

public class CompanyMapperTest {

	@Test
	public void createCompanyTest() throws SQLException {
		//given
		final int ID = 42;
		final String NAME = "toto";
		
		ResultSet rs = mock(ResultSet.class);
		doReturn(ID).when(rs).getInt("id");
		doReturn(NAME).when(rs).getString("name");
		
		//when
		Company company = CompanyMapper.INSTANCE.resultSetToCompany(rs);

		//then
		assertEquals(ID, company.getId());
		assertEquals(NAME, company.getName());
	}

}
