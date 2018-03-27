package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.model.Company;

public interface CompanyDao {

    List<Company> list(int offset, int nbToPrint) throws DaoException;
    Optional<Company> read(long companyId) throws DaoException;
    Optional<Company> findByName(String companyName) throws DaoException;
    public long count() throws DaoException;
}
