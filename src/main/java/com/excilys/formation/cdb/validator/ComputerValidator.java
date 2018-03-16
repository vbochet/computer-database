package com.excilys.formation.cdb.validator;

import com.excilys.formation.cdb.model.Computer;

public enum ComputerValidator {

	INSTANCE;
	
	public boolean validateComputer(Computer c) {
		if(validateComputerName(c)) {
			if(validateComputerDates(c)) {
				return true;
			}
			return false;
		}
		
		return false;
	}
	
	public boolean validateComputerName(Computer c) {
		if(c.getName().isEmpty()) {
			System.err.println("A computer must have a name!");
			return false;
		}
		return true;
	}
	
	public boolean validateComputerDates(Computer c) {
		if((c.getDiscontinued() != null) && 
				((c.getIntroduced() == null) ||
					(c.getIntroduced().compareTo(c.getDiscontinued()) > 0))) {
			System.err.println("The discontinuation date must be greater than the introduction date!");
			return false;
		}
		return true;
	}

}
