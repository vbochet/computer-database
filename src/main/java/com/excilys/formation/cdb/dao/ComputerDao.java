package com.excilys.formation.cdb.dao;

import java.util.List;

import com.excilys.formation.cdb.model.Computer;

public interface ComputerDao {

    Computer create(Computer c);
    Computer read(long id);
    Computer update(Computer c);
    void delete(long id);
    List<Computer> list(int offset, int nbToPrint);

}
