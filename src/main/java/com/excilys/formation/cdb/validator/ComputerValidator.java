package com.excilys.formation.cdb.validator;

import com.excilys.formation.cdb.model.Computer;

public enum ComputerValidator {

    INSTANCE;

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
            System.err.println("A computer must have a name!");
            return false;
        }
        return true;
    }

    public boolean validateComputerDates(Computer computer) {
        if ((computer.getDiscontinued() != null) && 
                ((computer.getIntroduced() == null) ||
                    (computer.getIntroduced().compareTo(computer.getDiscontinued()) > 0))) {
            System.err.println("The discontinuation date must be greater than the introduction date!");
            return false;
        }
        return true;
    }
}
