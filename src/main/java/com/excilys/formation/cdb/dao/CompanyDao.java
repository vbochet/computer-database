package com.excilys.formation.cdb.dao;

import java.util.List;

import com.excilys.formation.cdb.model.Company;

public interface CompanyDao {

    List<Company> list(int offset, int nbToPrint);
    Company read(long companyId);
    Company findByName(String companyName);
    public long count();
}
