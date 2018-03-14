package com.excilys.formation.cdb.dao;

import java.util.List;

import com.excilys.formation.cdb.model.Company;

public interface CompanyDao {

	public List<Company> list();
	public List<Company> list(int offset);
	public List<Company> list(int offset, int nbToPrint);
	
	public Company read(long companyId);
	
}
