package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.excilys.formation.cdb.model.Company;

public class RowCompanyMapper implements RowMapper<Company> {

    static final Logger LOGGER = LoggerFactory.getLogger(RowCompanyMapper.class);

    @Override
    public Company mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return CompanyMapper.INSTANCE.resultSetToCompany(resultSet);
    }

}
