package com.excilys.formation.cdb.service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Company> getById(long companyId) {
        Optional<Company> ret = Optional.empty();
        if (companyId > 0) {
            ret = CompanyDaoImpl.INSTANCE.read(companyId);
        }

        return ret;
    }

    public Optional<Company> getByName(String companyName) {
        Optional<Company> ret = Optional.empty();
        if (companyName != null && !companyName.isEmpty()) {
            ret = CompanyDaoImpl.INSTANCE.findByName(companyName);
        }

        return ret;
    }

    public long getNbFound() {
        return CompanyDaoImpl.INSTANCE.count();
    }
}
