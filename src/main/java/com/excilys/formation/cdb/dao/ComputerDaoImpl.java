package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.persistence.ConnectionManager;

public enum ComputerDaoImpl implements ComputerDao {

	INSTANCE;
	
	private final String CREATE_REQUEST = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);";
	private final String READ_REQUEST   = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name as company_name FROM computer LEFT JOIN company ON company.id=computer.company_id WHERE computer.id = ?;";
	private final String UPDATE_REQUEST = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
	private final String DELETE_REQUEST = "DELETE FROM computer WHERE id = ?;";
	private final String LIST_REQUEST   = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name as company_name FROM computer LEFT JOIN company ON company.id=computer.company_id LIMIT ? OFFSET ?;";
	
	@Override
	public Computer create(Computer c) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
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
		LocalDate intro, discont;
		
		ps = conn.prepareStatement(CREATE_REQUEST, Statement.RETURN_GENERATED_KEYS);
		
		ps.setString(1, c.getName());
		
		intro = c.getIntroduced();
		if(intro == null) {
			ps.setNull(2, java.sql.Types.DATE);
		}
		else {
			ps.setDate(2, Date.valueOf(intro));
		}
		
		discont = c.getDiscontinued();
		if(discont == null) {
			ps.setNull(3, java.sql.Types.DATE);
		}
		else {
			ps.setDate(3, Date.valueOf(discont));
		}

		if(company == null) {
			ps.setNull(4, java.sql.Types.BIGINT);
		}
		else {
			ps.setLong(4, company.getId());
		}
		
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
		
		ps = conn.prepareStatement(READ_REQUEST);
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
		
		try {
			executeUpdateRequest(conn, ps, rs, c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
		return c;
	}
	
	private int executeUpdateRequest(Connection conn, PreparedStatement ps, ResultSet rs, Computer c) throws SQLException {
		Company company = c.getCompany();
		LocalDate intro, discont;
		
		ps = conn.prepareStatement(UPDATE_REQUEST);
	    
		ps.setString(1, c.getName());
		
		intro = c.getIntroduced();
		if(intro == null) {
			ps.setNull(2, java.sql.Types.DATE);
		}
		else {
			ps.setDate(2, Date.valueOf(intro));
		}
		
		discont = c.getDiscontinued();
		if(discont == null) {
			ps.setNull(3, java.sql.Types.DATE);
		}
		else {
			ps.setDate(3, Date.valueOf(discont));
		}

		if(company == null) {
			ps.setNull(4, java.sql.Types.BIGINT);
		}
		else {
			ps.setLong(4, company.getId());
		}
		
	    ps.setLong(5, c.getId());
	    
	    return ps.executeUpdate();
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
		ps = conn.prepareStatement(DELETE_REQUEST);
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
		ps = conn.prepareStatement(LIST_REQUEST);
		ps.setInt(1, nbToPrint);
		ps.setLong(2, offset);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			computersList.add(ComputerMapper.INSTANCE.createComputer(rs));
		}
	}

}
