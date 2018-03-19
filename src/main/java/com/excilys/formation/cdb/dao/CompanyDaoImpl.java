package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.persistence.ConnectionManager;

public enum CompanyDaoImpl implements CompanyDao {

	INSTANCE;

    static Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);
	
	private final String LIST_REQUEST = "SELECT * FROM company LIMIT ? OFFSET ?;";
	private final String READ_REQUEST = "SELECT * FROM company WHERE id = ?;";
	
	@Override
	public List<Company> list(int offset, int nbToPrint) {
		LOGGER.info("Listing companies from "+offset+" ("+nbToPrint+" per page)");
		
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Company> companiesList = new ArrayList<>();
		
		try {
			executeListRequest(conn, ps, rs, offset, nbToPrint, companiesList);
		} catch (SQLException e) {
			LOGGER.error("SQL error in companies listing");
			LOGGER.error(e.getLocalizedMessage());
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
		return companiesList;
	}

	private void executeListRequest(Connection conn, PreparedStatement ps, ResultSet rs,int offset, int nbToPrint, List<Company> companiesList) throws SQLException {
		ps = conn.prepareStatement(LIST_REQUEST);
		ps.setInt(1, nbToPrint);
		ps.setInt(2, offset);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			companiesList.add(CompanyMapper.INSTANCE.createCompany(rs));
		}
	}

	@Override
	public Company read(long companyId) {
		LOGGER.info("Showing info from company n°"+companyId);
		
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Company company = null;
		
		try {
			company = executeReadRequest(conn, ps, rs, companyId);
		} catch (SQLException e) {
			LOGGER.error("SQL error in company reading");
			LOGGER.error(e.getLocalizedMessage());
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
		return company;
	};

	private Company executeReadRequest(Connection conn, PreparedStatement ps, ResultSet rs, long id) throws SQLException {
		ps = conn.prepareStatement(READ_REQUEST);
		ps.setLong(1, id);
		rs = ps.executeQuery();
		
		if(rs.first()) {
			return CompanyMapper.INSTANCE.createCompany(rs);
		}
		
		return null;
	}
	
}
