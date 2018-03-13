package com.excilys.formation.cdb.dao;

import java.util.List;

import com.excilys.formation.cdb.model.Company;

public interface CompanyDao {

	public List<Company> list();
	public List<Company> list(long id_first);
	public List<Company> list(long id_first, int nb_to_print);
	
}
