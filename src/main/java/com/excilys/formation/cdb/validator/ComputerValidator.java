package com.excilys.formation.cdb.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.model.Computer;

public enum ComputerValidator {

    INSTANCE;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerValidator.class);

    public boolean validateComputer(Computer computer) {
        if (validateComputerName(computer)) {
            if (validateComputerDates(computer)) {
                return true;
            }
            return false;
        }

        return false;
    }

    public boolean validateComputerName(Computer computer) {
        if (computer.getName() == null || computer.getName().isEmpty()) {
            LOGGER.error("A computer must have a name!");
            return false;
        }
        return true;
    }

    public boolean validateComputerDates(Computer computer) {
        if ((computer.getDiscontinued() != null) && 
                ((computer.getIntroduced() == null) ||
                    (computer.getIntroduced().compareTo(computer.getDiscontinued()) > 0))) {
            LOGGER.error("The discontinuation date must be greater than the introduction date!");
            return false;
        }
        return true;
    }
}
