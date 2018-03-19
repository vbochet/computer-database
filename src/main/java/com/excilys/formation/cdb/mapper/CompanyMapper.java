package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.formation.cdb.model.Company;

public enum CompanyMapper {

    INSTANCE;

    public Company createCompany(ResultSet result) throws SQLException {
        return new Company(result.getInt("id"), result.getString("name"));
    }
}
