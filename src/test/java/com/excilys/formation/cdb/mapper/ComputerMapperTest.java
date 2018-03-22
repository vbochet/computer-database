package com.excilys.formation.cdb.mapper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.excilys.formation.cdb.dto.ComputerDto;
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
        final String companyName = "RCA";
        final Company COMPANY = new Company(companyId,companyName);
        
        Computer computer;
        ComputerDto computerDto;

        //when
        computer = new Computer(ID, NAME, DATE_1, DATE_2, COMPANY);
        computerDto = ComputerMapper.INSTANCE.computerToComputerDto(computer);

        //then
        assertEquals(ID, computerDto.getComputerId());
        assertEquals(NAME, computerDto.getComputerName());
        assertEquals(DATE_1.toString(), computerDto.getComputerIntroduced().toString());
        assertEquals(DATE_2.toString(), computerDto.getComputerDiscontinued().toString());
        assertEquals(companyName, computerDto.getComputerCompanyName());


        //when
        computer = new Computer(NO_ID, null, null, null, null);
        computerDto = ComputerMapper.INSTANCE.computerToComputerDto(computer);

        //then
        assertEquals(NO_ID, computerDto.getComputerId());
        assertEquals(null, computerDto.getComputerName());
        assertEquals(null, computerDto.getComputerIntroduced());
        assertEquals(null, computerDto.getComputerDiscontinued());
        assertEquals(null, computerDto.getComputerCompanyName());

    }


    @Test
    public void computerDtoToComputerTest() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        //given
        final int ID = 42;
        final int NO_ID = 0;
        final String NAME = "toto";
        final LocalDate DATE_1 = LocalDate.parse("2000-01-01", formatter);
        final LocalDate DATE_2 = LocalDate.parse("2010-05-03", formatter);
        final int COMPANY_ID = 3;
        final String COMPANY_NAME = "RCA";
        
        Computer computer;
        ComputerDto computerDto;

        //when
        computerDto = new ComputerDto();
        computerDto.setComputerId(ID);
        computerDto.setComputerName(NAME);
        computerDto.setComputerIntroduced(DATE_1.toString());
        computerDto.setComputerDiscontinued(DATE_2.toString());
        computerDto.setComputerCompany(COMPANY_NAME);
        
        computer = ComputerMapper.INSTANCE.computerDtoToComputer(computerDto);

        //then
        assertEquals(ID, computer.getId());
        assertEquals(NAME, computer.getName());
        assertEquals(DATE_1, computer.getIntroduced());
        assertEquals(DATE_2, computer.getDiscontinued());
        assertEquals(COMPANY_ID, computer.getCompany().getId());
        assertEquals(COMPANY_NAME, computer.getCompany().getName());


        //when
        computerDto = new ComputerDto();
        computerDto.setComputerId(NO_ID);
        computerDto.setComputerName(null);
        computerDto.setComputerIntroduced(null);
        computerDto.setComputerDiscontinued(null);
        computerDto.setComputerCompany(null);
        
        computer = ComputerMapper.INSTANCE.computerDtoToComputer(computerDto);

        //then
        assertEquals(NO_ID, computer.getId());
        assertEquals(null, computer.getName());
        assertEquals(null, computer.getIntroduced());
        assertEquals(null, computer.getDiscontinued());
        assertEquals(null, computer.getCompany());


        //when
        computerDto = new ComputerDto();
        computerDto.setComputerId(ID);
        computerDto.setComputerName(NAME);
        computerDto.setComputerIntroduced(DATE_2.toString());
        computerDto.setComputerDiscontinued(DATE_1.toString());
        computerDto.setComputerCompany(COMPANY_NAME);
        
        computer = ComputerMapper.INSTANCE.computerDtoToComputer(computerDto);

        //then
        assertEquals(ID, computer.getId());
        assertEquals(NAME, computer.getName());
        assertEquals(DATE_2, computer.getIntroduced());
        assertEquals(DATE_1, computer.getDiscontinued());
        assertEquals(COMPANY_ID, computer.getCompany().getId());
        assertEquals(COMPANY_NAME, computer.getCompany().getName());



    }

}
