package com.excilys.formation.cdb.dao;

import java.util.List;

import com.excilys.formation.cdb.model.Company;

public interface CompanyDao {

	public List<Company> list();
	public List<Company> list(long idFirst);
	public List<Company> list(long idFirst, int nbToPrint);
	
}
