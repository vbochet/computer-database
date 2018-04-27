package com.excilys.formation.cdb.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.formation.cdb.dao.ComputerDao;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.validator.ComputerValidator;

@Service("computerServiceBean")
@EnableTransactionManagement
public class ComputerService {
    @Autowired
    private ComputerDao computerDao;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerService.class);

    public List<Computer> getList(int offset, int nbToPrint, String order, boolean desc) {
        if (nbToPrint >= 1) {
            return computerDao.list(offset, nbToPrint, order, desc);
        }

        return new ArrayList<>();
    }

    public List<Computer> getSearchList(int offset, int nbToPrint, String order, boolean desc, String search) {
        if (nbToPrint >= 1) {
            return computerDao.listSearch(offset, nbToPrint, order, desc, search);
        }

        return new ArrayList<>();
    }

    public Optional<Computer> getById(long id) {
        if (id > 0) {
            return computerDao.read(id);
        }

        return Optional.empty();
    }

    public long getNbFound() {
        return computerDao.count();
    }

    public long getNbSearch(String search) {
        return computerDao.countSearch(search);
    }

    @Transactional
    public boolean deleteById(long id) {
        if (id > 0) {
            computerDao.delete(id);
            return true;
        }

        return false;
    }

    public void setName(String name, Computer computer) {
        if (name.isEmpty()) {
            LOGGER.error("Name is mandatory, aborting creation");
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

    @Transactional
    public Computer createComputer(Computer computer) {
        if (!ComputerValidator.INSTANCE.validateComputer(computer)) {
            return null;
        }

        return computerDao.create(computer);
    }

    @Transactional
    public Computer updateComputer(Computer computer) {
        if (!ComputerValidator.INSTANCE.validateComputer(computer)) {
            return null;
        }

        return computerDao.update(computer);
    }

    @Transactional
    public boolean deleteManyById(List<Long> ids) {
        boolean elementsValid = true;
        for(Long id : ids) {
            elementsValid = elementsValid && (id > 0);
        }
        
        if (elementsValid) {
            computerDao.deleteMany(ids);
            return true;
        }

        return false;
    }
}
