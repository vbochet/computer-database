package com.excilys.formation.cdb.dao;

import java.util.List;

import com.excilys.formation.cdb.model.Computer;

public interface ComputerDao {
	
	public Computer create(Computer c);
	
	public Computer read(long id);
	
	public Computer update(Computer c);
	
	public void delete(long id);

	public List<Computer> list();
	public List<Computer> list(int offset);
	public List<Computer> list(int offset, int nbToPrint);
	
}
