package com.excilys.formation.cdb.mapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.formation.cdb.configuration.TestBindingConfig;
import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.model.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBindingConfig.class})
public class CompanyMapperTest {

	@Autowired
	CompanyMapper companyMapper;

	@Test
	public void resultSetToCompanyTest() throws SQLException {
		//given
		final int ID = 42;
		final String NAME = "toto";
		
		ResultSet rs = mock(ResultSet.class);
		doReturn(ID).when(rs).getInt("id");
		doReturn(NAME).when(rs).getString("name");
		
		//when
		Company company = companyMapper.resultSetToCompany(rs);

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
        companyDto = companyMapper.companyToCompanyDto(company);

        //then
        assertEquals(ID, companyDto.getCompanyId());
        assertEquals(NAME, companyDto.getCompanyName());


        //when
        company = new Company(NO_ID, null);
        companyDto = companyMapper.companyToCompanyDto(company);

        //then
        assertEquals(NO_ID, companyDto.getCompanyId());
        assertEquals(null, companyDto.getCompanyName());
    }

}
