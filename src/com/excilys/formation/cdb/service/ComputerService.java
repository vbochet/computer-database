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
	
	public List<Computer> getList(long idFirst, int nbToPrint) {
		if(nbToPrint >= 1) {
			return ComputerDaoImpl.INSTANCE.list(idFirst, nbToPrint);
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

	public Computer setName(String name, Computer c) {
		if(name.isEmpty()) {
			System.err.println("Name is mandatory, aborting creation");
		}
		else {
			c.setName(name);
		}
		
		return c;
	}

	public boolean setIntroDate(String intro, DateFormat dateFormat, Computer c) {
		Date date;
		long time;
		if(!intro.equals("_")) {
			try {
				date = dateFormat.parse(intro);
				time = date.getTime();
				Timestamp introduced = new Timestamp(time);
				c.setIntroduced(introduced);
			} catch (ParseException e) {
				return false;
			}
		}
		
		return true;
	}

	public boolean setDiscontDate(String discont, DateFormat dateFormat, Computer c) {
		Date date;
		long time;
		if(!discont.equals("_")) {
			try {
				date = dateFormat.parse(discont);
				time = date.getTime();
				Timestamp discontinued = new Timestamp(time);
				c.setIntroduced(discontinued);
			} catch (ParseException e) {
				return false;
			}
		}
		
		return true;
	}
}
