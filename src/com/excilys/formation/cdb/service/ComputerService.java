package com.excilys.formation.cdb.service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.excilys.formation.cdb.dao.ComputerDaoImpl;
import com.excilys.formation.cdb.model.Computer;

public enum ComputerService {
	INSTANCE;
	
	public List<Computer> getList(int offset, int nbToPrint) {
		if(nbToPrint >= 1) {
			return ComputerDaoImpl.INSTANCE.list(offset, nbToPrint);
		}
		
		return null;
	}
	
	public Computer getById(long id) {
		if(id > 0) {
			return ComputerDaoImpl.INSTANCE.read(id);
		}
		
		return null;
	}
	
	public boolean deleteById(long id) {
		if(id > 0) {
			ComputerDaoImpl.INSTANCE.delete(id);
			return true;
		}
		
		return false;
	}

	public void setName(String name, Computer c) {
		if(name.isEmpty()) {
			System.err.println("Name is mandatory, aborting creation");
		}
		else {
			c.setName(name);
		}
	}

	public boolean setIntroDate(String intro, DateFormat dateFormat, Computer c) {
		if(!intro.isEmpty()) {
			try {
				c.setIntroduced(LocalDate.parse(intro));
			} catch (DateTimeParseException e) {
				return false;
			}
		}
		else {
			c.setIntroduced(null);
		}
		
		return true;
	}

	public boolean setDiscontDate(String discont, DateFormat dateFormat, Computer c) {
		if(!discont.isEmpty()) {
			try {
				c.setDiscontinued(LocalDate.parse(discont));
			} catch (DateTimeParseException e) {
				return false;
			}
		}
		else {
			c.setDiscontinued(null);
		}
		
		return true;
	}
}
