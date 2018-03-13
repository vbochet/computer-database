package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.formation.cdb.model.Computer;

public enum ComputerMapper {

	INSTANCE; 
	
	public Computer createComputer(ResultSet result) throws SQLException {
		Computer c = new Computer();
		c.setId(result.getLong("id"));
		c.setName(result.getString("name"));
		c.setIntroduced(result.getTimestamp("introduced"));
		c.setDiscontinued(result.getTimestamp("discontinued"));
		c.setCompany_id(result.getInt("company_id"));
		
		return c;
	}
}
