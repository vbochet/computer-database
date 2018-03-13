package com.excilys.formation.cdb.service;

import java.util.List;

import com.excilys.formation.cdb.dao.CompanyDaoImpl;
import com.excilys.formation.cdb.model.Company;

public enum CompanyService {
	INSTANCE;
	
	public List<Company> getList(long idFirst, int nbToPrint) {
		if(idFirst > 0 && nbToPrint >= 1) {
			return CompanyDaoImpl.INSTANCE.list(idFirst, nbToPrint);
		}
		
		return null;
	}
}
