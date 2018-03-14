package com.excilys.formation.cdb.service;

import java.util.List;

import com.excilys.formation.cdb.dao.CompanyDaoImpl;
import com.excilys.formation.cdb.model.Company;

public enum CompanyService {
	INSTANCE;
	
	public List<Company> getList(long idFirst, int nbToPrint) {
		if(nbToPrint >= 1) {
			return CompanyDaoImpl.INSTANCE.list(idFirst, nbToPrint);
		}
		
		return null;
	}

	public Company getById(long companyId) {
		Company ret = null;
		if(companyId > 0) {
			List<Company> cl = CompanyDaoImpl.INSTANCE.list(companyId, 1);
			if(cl.size() > 0) {
				ret = cl.get(0);
			}
		}
		
		return ret;
	}
}
