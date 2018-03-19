package com.excilys.formation.cdb.validator;

import static org.junit.Assert.*;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.Test;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;

public class ComputerValidatorTest {

	@Test
	public void validateComputerTest() {
		final long ID = 999;
		final String NAME = "toto";
		final String EMPTY_NAME = "";
		final String NO_NAME = null;
		final LocalDate DATE_1 = new Date(0).toLocalDate();
		final LocalDate DATE_2 = new Date(100000000).toLocalDate();
		final LocalDate NO_DATE = null;
		final Company COMPANY = new Company(3,"RCA");
		final Company NO_COMPANY = null;
		
		Computer computer;
		boolean checkName, checkDate, checkAll;
		
		
		
		//given
		computer = new Computer(ID, NAME, DATE_1, DATE_2, COMPANY);
		
		//when
		checkName = ComputerValidator.INSTANCE.validateComputerName(computer);
		checkDate = ComputerValidator.INSTANCE.validateComputerDates(computer);
		checkAll = ComputerValidator.INSTANCE.validateComputer(computer);
		
		//then
		assertTrue(checkName);
		assertTrue(checkDate);
		assertTrue(checkAll);
		
		
		
		//given
		computer = new Computer(ID, NAME, NO_DATE, NO_DATE, NO_COMPANY);
		
		//when
		checkName = ComputerValidator.INSTANCE.validateComputerName(computer);
		checkDate = ComputerValidator.INSTANCE.validateComputerDates(computer);
		checkAll = ComputerValidator.INSTANCE.validateComputer(computer);
		
		//then
		assertTrue(checkName);
		assertTrue(checkDate);
		assertTrue(checkAll);
		
		
		
		//given
		computer = new Computer(ID, NAME, DATE_1, NO_DATE, NO_COMPANY);
		
		//when
		checkName = ComputerValidator.INSTANCE.validateComputerName(computer);
		checkDate = ComputerValidator.INSTANCE.validateComputerDates(computer);
		checkAll = ComputerValidator.INSTANCE.validateComputer(computer);
		
		//then
		assertTrue(checkName);
		assertTrue(checkDate);
		assertTrue(checkAll);
		
		
		
		//given
		computer = new Computer(ID, NAME, DATE_1, DATE_1, NO_COMPANY);
		
		//when
		checkName = ComputerValidator.INSTANCE.validateComputerName(computer);
		checkDate = ComputerValidator.INSTANCE.validateComputerDates(computer);
		checkAll = ComputerValidator.INSTANCE.validateComputer(computer);
		
		//then
		assertTrue(checkName);
		assertTrue(checkDate);
		assertTrue(checkAll);
		
		
		
		//given
		computer = new Computer(ID, NAME, DATE_2, DATE_1, NO_COMPANY);
		
		//when
		checkName = ComputerValidator.INSTANCE.validateComputerName(computer);
		checkDate = ComputerValidator.INSTANCE.validateComputerDates(computer);
		checkAll = ComputerValidator.INSTANCE.validateComputer(computer);
		
		//then
		assertTrue(checkName);
		assertFalse(checkDate);
		assertFalse(checkAll);

		
		
		//given
		computer = new Computer(ID, NAME, NO_DATE, DATE_1, NO_COMPANY);
		
		//when
		checkName = ComputerValidator.INSTANCE.validateComputerName(computer);
		checkDate = ComputerValidator.INSTANCE.validateComputerDates(computer);
		checkAll = ComputerValidator.INSTANCE.validateComputer(computer);
		
		//then
		assertTrue(checkName);
		assertFalse(checkDate);
		assertFalse(checkAll);
		
		
		
		//given
		computer = new Computer(ID, EMPTY_NAME, NO_DATE, NO_DATE, NO_COMPANY);
		
		//when
		checkName = ComputerValidator.INSTANCE.validateComputerName(computer);
		checkDate = ComputerValidator.INSTANCE.validateComputerDates(computer);
		checkAll = ComputerValidator.INSTANCE.validateComputer(computer);
		
		//then
		assertFalse(checkName);
		assertTrue(checkDate);
		assertFalse(checkAll);
		
		
		
		//given
		computer = new Computer(ID, NO_NAME, NO_DATE, NO_DATE, NO_COMPANY);
		
		//when
		checkName = ComputerValidator.INSTANCE.validateComputerName(computer);
		checkDate = ComputerValidator.INSTANCE.validateComputerDates(computer);
		checkAll = ComputerValidator.INSTANCE.validateComputer(computer);
		
		//then
		assertFalse(checkName);
		assertTrue(checkDate);
		assertFalse(checkAll);
		
	}
}
