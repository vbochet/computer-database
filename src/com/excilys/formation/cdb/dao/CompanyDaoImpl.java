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

public class CompanyDaoImpl implements CompanyDao {

	@Override
	public List<Company> list(int id_first, int nb_to_print) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Company> companiesList = new ArrayList<>();
		
		try {
			ps = conn.prepareStatement("SELECT * FROM company WHERE id >= ? LIMIT ?");
			ps.setInt(1, id_first);
			ps.setInt(2, nb_to_print);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				companiesList.add(CompanyMapper.createCompany(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(null, ps, rs);
		}
		
		return companiesList;
	}

	@Override
	public List<Company> list(int id_first) {
		return this.list(id_first, 10);
	}

	@Override
	public List<Company> list() {
		return this.list(0, 10);
	};
	
}
