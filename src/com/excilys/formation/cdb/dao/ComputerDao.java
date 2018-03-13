package com.excilys.formation.cdb.dao;

import java.util.List;

import com.excilys.formation.cdb.model.Computer;

public interface ComputerDao {
	
	public Computer create(Computer c);
	
	public Computer read(int id);
	
	public Computer update(Computer c);
	
	public void delete(int id);

	public List<Computer> list();
	public List<Computer> list(int id_first);
	public List<Computer> list(int id_first, int nb_to_print);
	
}
