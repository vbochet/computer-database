package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import com.excilys.formation.cdb.model.Company;

public interface CompanyDao {

    List<Company> list(int offset, int nbToPrint);
    Optional<Company> read(long companyId);
    Optional<Company> findByName(String companyName);
    public long count();
}
