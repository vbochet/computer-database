package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.persistence.ConnectionManager;

public enum CompanyDaoImpl implements CompanyDao {

	INSTANCE;
	
	String listRequest = "SELECT * FROM company WHERE id >= ? LIMIT ?;";
	
	@Override
	public List<Company> list(long idFirst, int nbToPrint) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Company> companiesList = new ArrayList<>();
		
		try {
			executeListRequest(conn, ps, rs, idFirst, nbToPrint, companiesList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(conn, ps, rs);
		}
		
		return companiesList;
	}

	private void executeListRequest(Connection conn, PreparedStatement ps, ResultSet rs,long idFirst, int nbToPrint, List<Company> companiesList) throws SQLException {
		ps = conn.prepareStatement(listRequest);
		ps.setLong(1, idFirst);
		ps.setInt(2, nbToPrint);
		rs = ps.executeQuery();
		
		while(rs.next()) {
			companiesList.add(CompanyMapper.INSTANCE.createCompany(rs));
		}
	}
	
	@Override
	public List<Company> list(long idFirst) {
		return this.list(idFirst, 10);
	}

	@Override
	public List<Company> list() {
		return this.list(0, 10);
	};
	
}
