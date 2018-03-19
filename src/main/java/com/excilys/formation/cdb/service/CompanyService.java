package com.excilys.formation.cdb.service;

import java.util.List;

import com.excilys.formation.cdb.dao.CompanyDaoImpl;
import com.excilys.formation.cdb.model.Company;

public enum CompanyService {
    INSTANCE;

    public List<Company> getList(int offset, int nbToPrint) {
        if (nbToPrint >= 1) {
            return CompanyDaoImpl.INSTANCE.list(offset, nbToPrint);
        }

        return null;
    }

    public Company getById(long companyId) {
        Company ret = null;
        if (companyId > 0) {
            ret = CompanyDaoImpl.INSTANCE.read(companyId);
        }

        return ret;
    }
}
