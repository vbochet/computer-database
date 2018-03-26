package com.excilys.formation.cdb.mapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.model.Company;

public class CompanyMapperTest {

	@Test
	public void resultSetToCompanyTest() throws SQLException {
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


    @Test
    public void companyToCompanyDtoTest() throws SQLException {
        //given
        final int ID = 42;
        final int NO_ID = 0;
        final String NAME = "toto";
        Company company;
        CompanyDto companyDto;

        //when
        company = new Company(ID, NAME);
        companyDto = CompanyMapper.INSTANCE.companyToCompanyDto(company);

        //then
        assertEquals(ID, companyDto.getCompanyId());
        assertEquals(NAME, companyDto.getCompanyName());


        //when
        company = new Company(NO_ID, null);
        companyDto = CompanyMapper.INSTANCE.companyToCompanyDto(company);

        //then
        assertEquals(NO_ID, companyDto.getCompanyId());
        assertEquals(null, companyDto.getCompanyName());
    }


    @Test
    public void companyDtoToCompanyTest() throws SQLException {
        //given
        final int ID = 42;
        final int NO_ID = 0;
        final String NAME = "toto";
        Company company;
        CompanyDto companyDto;

        //when
        companyDto = new CompanyDto();
        companyDto.setCompanyId(ID);
        companyDto.setCompanyName(NAME);
        company = CompanyMapper.INSTANCE.companyDtoToCompany(companyDto);

        //then
        assertEquals(ID, company.getId());
        assertEquals(NAME, company.getName());


        //when
        companyDto = new CompanyDto();
        companyDto.setCompanyId(NO_ID);
        companyDto.setCompanyName(null);
        company = CompanyMapper.INSTANCE.companyDtoToCompany(companyDto);

        //then
        assertEquals(NO_ID, company.getId());
        assertEquals(null, company.getName());
    }

}
