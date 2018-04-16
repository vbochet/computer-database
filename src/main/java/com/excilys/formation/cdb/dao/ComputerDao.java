package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import com.excilys.formation.cdb.model.Computer;

public interface ComputerDao {

    Computer create(Computer computer);
    Optional<Computer> read(long id);
    Computer update(Computer computer);
    void delete(long id);
    List<Computer> list(int offset, int nbToPrint, String order, boolean desc);
    public long count();
    void deleteMany(List<Long> id);
    List<Computer> listSearch(int offset, int nbToPrint, String order, boolean desc, String search);
    long countSearch(String search);

}
