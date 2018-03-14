package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.persistence.ConnectionManager;
import com.excilys.formation.cdb.validator.ComputerValidator;

public enum ComputerDaoImpl implements ComputerDao {

	INSTANCE;
	
	private String createRequest = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);",
				   readRequest   = "SELECT computer.*, company.name as company_name FROM computer LEFT JOIN company ON company.id=computer.company_id WHERE computer.id = ?;",
				   updateRequest = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;",
				   deleteRequest = "DELETE FROM computer WHERE id = ?;",
				   listRequest   = "SELECT computer.*, company.name as company_name FROM computer LEFT JOIN company ON company.id=computer.company_id LIMIT ? OFFSET ?;";
	
	@Override
	public Computer create(Computer c) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		if(!ComputerValidator.INSTANCE.validateComputer(c)) {
			return null;
		}
		
		try {
			executeCreateRequest(conn, ps, rs, c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
	    return c;
	}
	
	private void executeCreateRequest(Connection conn, PreparedStatement ps, ResultSet rs, Computer c) throws SQLException {
		Company company = c.getCompany();
		long companyId = company == null ? 0 : company.getId();
		
		ps = conn.prepareStatement(createRequest, Statement.RETURN_GENERATED_KEYS);
		
		ps.setString(1, c.getName());
		ps.setTimestamp(2, c.getIntroduced());
		ps.setTimestamp(3, c.getDiscontinued());
		ps.setLong(4, companyId);
		
		ps.executeUpdate();
		
		rs = ps.getGeneratedKeys();
		if(rs.first()) {
			c.setId(rs.getLong(1));
		}
	}

	@Override
	public Computer read(long id) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Computer res = null;

		try {
			res = executeReadRequest(conn, ps, rs, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
		return res;
	}
	
	private Computer executeReadRequest(Connection conn, PreparedStatement ps, ResultSet rs, long id) throws SQLException {
		Computer c = null;
		
		ps = conn.prepareStatement(readRequest);
		ps.setLong(1, id);
		rs = ps.executeQuery();
		
		if(rs.first()) {
			c = ComputerMapper.INSTANCE.createComputer(rs);
		}
		
		return c;
	}

	@Override
	public Computer update(Computer c) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Computer res = null;
		
		try {
			res = executeUpdateRequest(conn, ps, rs, c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
		return res;
	}
	
	private Computer executeUpdateRequest(Connection conn, PreparedStatement ps, ResultSet rs, Computer c) throws SQLException {
		Company company = c.getCompany();
		long companyId = company == null ? 0 : company.getId();
		
		ps = conn.prepareStatement(updateRequest, Statement.RETURN_GENERATED_KEYS);
	    
		ps.setString(1, c.getName());
	    ps.setTimestamp(2, c.getIntroduced());
	    ps.setTimestamp(3, c.getDiscontinued());
	    ps.setLong(4, companyId);
	    ps.setLong(5, c.getId());
	    
	    ps.executeUpdate();
		
		rs = ps.getGeneratedKeys();
		
		if(rs.first()) {
			c = ComputerMapper.INSTANCE.createComputer(rs);
		}
		return c;
	}

	@Override
	public void delete(long id) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		
		try {
			executeDeleteRequest(conn, ps, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, null);
		}
	}
	
	private void executeDeleteRequest(Connection conn, PreparedStatement ps, Long id) throws SQLException {
		ps = conn.prepareStatement(deleteRequest);
	    ps.setLong(1, id);
		ps.executeUpdate();
	}

	@Override
	public List<Computer> list(int offset, int nbToPrint) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Computer> computersList = new ArrayList<>();

		try {
			executeListRequest(conn, ps, rs, offset, nbToPrint, computersList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
		return computersList;
	}
	
	private void executeListRequest(Connection conn, PreparedStatement ps, ResultSet rs, int offset, int nbToPrint, List<Computer> computersList) throws SQLException {
		ps = conn.prepareStatement(listRequest);
		ps.setInt(1, nbToPrint);
		ps.setLong(2, offset);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			computersList.add(ComputerMapper.INSTANCE.createComputer(rs));
		}
	}
		

	@Override
	public List<Computer> list(int offset) {
		return this.list(offset, 10);
	}

	@Override
	public List<Computer> list() {
		return this.list(0, 10);
	}

}
