package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.model.Company;

@Component
public class CompanyMapper implements RowMapper<Company> {

    static final Logger LOGGER = LoggerFactory.getLogger(CompanyMapper.class);

    public Company resultSetToCompany(ResultSet result) throws SQLException {
        return new Company(result.getInt("id"), result.getString("name"));
    }

    public CompanyDto companyToCompanyDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        
        companyDto.setCompanyId(company.getId());
        companyDto.setCompanyName(company.getName());
        
        return companyDto;
    }

    @Override
    public Company mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSetToCompany(resultSet);
    }

}
