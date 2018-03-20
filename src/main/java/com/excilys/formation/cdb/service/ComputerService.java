package com.excilys.formation.cdb.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.excilys.formation.cdb.dao.ComputerDaoImpl;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.validator.ComputerValidator;

public enum ComputerService {
    INSTANCE;

    public List<Computer> getList(int offset, int nbToPrint) {
        if (nbToPrint >= 1) {
            return ComputerDaoImpl.INSTANCE.list(offset, nbToPrint);
        }

        return null;
    }

    public Computer getById(long id) {
        if (id > 0) {
            return ComputerDaoImpl.INSTANCE.read(id);
        }

        return null;
    }

    public long getNbFound() {
        return ComputerDaoImpl.INSTANCE.count();
    }

    public boolean deleteById(long id) {
        if (id > 0) {
            ComputerDaoImpl.INSTANCE.delete(id);
            return true;
        }

        return false;
    }

    public void setName(String name, Computer computer) {
        if (name.isEmpty()) {
            System.err.println("Name is mandatory, aborting creation");
        } else {
            computer.setName(name);
        }
    }

    public boolean setIntroDate(String intro, Computer computer) {
        if (!intro.isEmpty()) {
            try {
                computer.setIntroduced(LocalDate.parse(intro));
            } catch (DateTimeParseException e) {
                return false;
            }
        } else {
            computer.setIntroduced(null);
        }

        return true;
    }

    public boolean setDiscontDate(String discont, Computer computer) {
        if (!discont.isEmpty()) {
            try {
                computer.setDiscontinued(LocalDate.parse(discont));
            } catch (DateTimeParseException e) {
                return false;
            }
        } else {
            computer.setDiscontinued(null);
        }

        return true;
    }

    public Computer createComputer(Computer computer) {
        if (!ComputerValidator.INSTANCE.validateComputer(computer)) {
            return null;
        }

        return ComputerDaoImpl.INSTANCE.create(computer);
    }

    public Computer updateComputer(Computer computer) {
        if (!ComputerValidator.INSTANCE.validateComputer(computer)) {
            return null;
        }

        return ComputerDaoImpl.INSTANCE.update(computer);
    }
}
