package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.model.Computer;

public interface ComputerDao {

    Computer create(Computer computer) throws DaoException;
    Optional<Computer> read(long id) throws DaoException;
    Computer update(Computer computer) throws DaoException;
    void delete(long id) throws DaoException;
    List<Computer> list(int offset, int nbToPrint) throws DaoException;
    public long count() throws DaoException;
    void deleteMany(List<Long> id) throws DaoException;

}
