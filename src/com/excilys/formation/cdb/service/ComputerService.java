package com.excilys.formation.cdb.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
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
				Timestamp introduced = convertStringToTimestamp(intro, dateFormat);
				c.setIntroduced(introduced);
			} catch (ParseException e) {
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
				Timestamp discontinued = convertStringToTimestamp(discont, dateFormat);
				c.setDiscontinued(discontinued);
			} catch (ParseException e) {
				return false;
			}
		}
		else {
			c.setDiscontinued(null);
		}
		
		return true;
	}

	private Timestamp convertStringToTimestamp(String dateString, DateFormat dateFormat) throws ParseException {
		Date date;
		long time;
		
		date = dateFormat.parse(dateString);
		time = date.getTime();
		
		return new Timestamp(time);
	}
}
