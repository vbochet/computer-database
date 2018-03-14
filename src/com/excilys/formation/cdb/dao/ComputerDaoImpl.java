package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.persistence.ConnectionManager;
import com.excilys.formation.cdb.validator.ComputerValidator;

public enum ComputerDaoImpl implements ComputerDao {

	INSTANCE;
	
	private String createRequest = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);",
				   readRequest   = "SELECT * FROM computer WHERE id = ?;",
				   updateRequest = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;",
				   deleteRequest = "DELETE FROM computer WHERE id = ?;",
				   listRequest   = "SELECT * FROM computer WHERE id >= ? LIMIT ?;";
	
	@Override
	public Computer create(Computer c) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		if(!ComputerValidator.INSTANCE.validateComputer(c)) {
			return null;
		}
		
		try {
			c = executeCreateRequest(conn, ps, rs, c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
	    return c;
	}
	
	private Computer executeCreateRequest(Connection conn, PreparedStatement ps, ResultSet rs, Computer c) throws SQLException {
		ps = conn.prepareStatement(createRequest, Statement.RETURN_GENERATED_KEYS);
		
		ps.setString(1, c.getName());
		ps.setTimestamp(2, c.getIntroduced());
		ps.setTimestamp(3, c.getDiscontinued());
		ps.setLong(4, c.getCompany_id());
		
		ps.executeUpdate();
		
		rs = ps.getGeneratedKeys();
		if(rs.first()) {
			c.setId(rs.getLong(1));
		}
		
		return c;
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
		ps = conn.prepareStatement(updateRequest, Statement.RETURN_GENERATED_KEYS);
	    
		ps.setString(1, c.getName());
	    ps.setTimestamp(2, c.getIntroduced());
	    ps.setTimestamp(3, c.getDiscontinued());
	    ps.setLong(4, c.getCompany_id());
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
	public List<Computer> list(long idFirst, int nbToPrint) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Computer> computersList = new ArrayList<>();

		try {
			computersList = executeListRequest(conn, ps, rs, idFirst, nbToPrint);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
		return computersList;
	}
	
	private List<Computer> executeListRequest(Connection conn, PreparedStatement ps, ResultSet rs,long idFirst, int nbToPrint) throws SQLException {
		List<Computer> computersList = new ArrayList<>();
		
		ps = conn.prepareStatement(listRequest);
		ps.setLong(1, idFirst);
		ps.setInt(2, nbToPrint);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			computersList.add(ComputerMapper.INSTANCE.createComputer(rs));
		}
		
		return computersList;
	}
		

	@Override
	public List<Computer> list(long idFirst) {
		return this.list(idFirst, 10);
	}

	@Override
	public List<Computer> list() {
		return this.list(0, 10);
	}

}
