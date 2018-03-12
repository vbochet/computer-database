package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.formation.cdb.model.Computer;

public class ComputerMapper {

	public static Computer createComputer(ResultSet result) throws SQLException {
		return new Computer(result.getInt("id"), 
							result.getString("name"), 
							result.getTimestamp("introduced"), 
							result.getTimestamp("discontinued"), 
							result.getInt("company_id"));
	}
}
