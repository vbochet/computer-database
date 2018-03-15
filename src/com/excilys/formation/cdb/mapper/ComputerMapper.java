package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;

public enum ComputerMapper {

	INSTANCE; 
	
	public Computer createComputer(ResultSet result) throws SQLException {
		Computer c = new Computer();
		c.setId(result.getLong("id"));
		c.setName(result.getString("name"));
		c.setIntroduced(result.getTimestamp("introduced"));
		c.setDiscontinued(result.getTimestamp("discontinued"));
		c.setCompany(new Company(result.getInt("company_id"), result.getString("company_name")));
		
		return c;
	}
}
