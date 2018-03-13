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

public class ComputerDaoImpl implements ComputerDao {

	@Override
	public Computer create(Computer c) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;


		if(c.getName().isEmpty()) {
			System.err.println("A computer must have a name!");
			return null;
		}
		
		if(c.getDiscontinued() == null) {}
		else if((c.getIntroduced() == null) ||
			(c.getIntroduced().compareTo(c.getDiscontinued()) > 0)) {
			System.err.println("The discontinuation date must be greater than the introduction date!");
			return null;
		}
		
		try {
			ps = conn.prepareStatement("INSERT INTO computer (name, introduced, discontinued, company_id)"
													+ "VALUES(?, ?, ?, ?)", 
									   Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, c.getName());
			ps.setTimestamp(2, c.getIntroduced());
			ps.setTimestamp(3, c.getDiscontinued());
			ps.setLong(4, c.getCompany_id());
			
			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			if(rs.first()) {
				c.setId(rs.getLong(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(null, ps, rs);
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
			ps = conn.prepareStatement("SELECT * FROM computer WHERE id = ?");
			ps.setLong(1, id);
			rs = ps.executeQuery();
			
			if(rs.first()) {
				res = ComputerMapper.createComputer(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(null, ps, rs);
		}
		
		return res;
	}

	@Override
	public Computer update(Computer c) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Computer res = null;
		
		String sql = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
		try {
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		    
			ps.setString(1, c.getName());
		    ps.setTimestamp(2, c.getIntroduced());
		    ps.setTimestamp(3, c.getDiscontinued());
		    ps.setLong(4, c.getCompany_id());
		    ps.setLong(5, c.getId());
		    
		    ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			
			if(rs.first()) {
				res = ComputerMapper.createComputer(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(null, ps, rs);
		}
		
		return res;
	}

	@Override
	public void delete(long id) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		
		String sql = "DELETE FROM computer WHERE id = ?;";
		
		try {
			ps = conn.prepareStatement(sql);
		    ps.setLong(1, id);
		    
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(null, ps, null);
		}
	}


	@Override
	public List<Computer> list(long id_first, int nb_to_print) {
		Connection conn = ConnectionManager.INSTANCE.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Computer> computersList = new ArrayList<>();

		try {
			ps = conn.prepareStatement("SELECT * FROM computer WHERE id >= ? LIMIT ?");
			ps.setLong(1, id_first);
			ps.setInt(2, nb_to_print);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				computersList.add(ComputerMapper.createComputer(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.INSTANCE.closeElements(null, ps, rs);
		}
		
		return computersList;
	}

	@Override
	public List<Computer> list(long id_first) {
		return this.list(id_first, 10);
	}

	@Override
	public List<Computer> list() {
		return this.list(0, 10);
	}

}
