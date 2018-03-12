package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.persistence.ConnectionManager;

public class CompanyDaoImpl implements CompanyDao {

	@Override
	public List<Company> list() {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		ResultSet rs = null;
		List<Company> companiesList = new ArrayList<>();
		
		try {
			rs = conn.createStatement().executeQuery("SELECT * FROM company");
			
			while(rs.next()) {
				companiesList.add(CompanyMapper.createCompany(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(null, null, rs);
		}
		
		return companiesList;
	};
	
}
