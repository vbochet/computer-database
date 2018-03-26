package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.formation.cdb.dto.CompanyDto;
import com.excilys.formation.cdb.model.Company;

public enum CompanyMapper {

    INSTANCE;

    public Company resultSetToCompany(ResultSet result) throws SQLException {
        return new Company(result.getInt("id"), result.getString("name"));
    }

    public CompanyDto companyToCompanyDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        
        companyDto.setCompanyId(company.getId());
        companyDto.setCompanyName(company.getName());
        
        return companyDto;
    }

    public Company companyDtoToCompany(CompanyDto companyDto) {
        Company company = new Company();
        
        company.setId(companyDto.getCompanyId());
        company.setName(companyDto.getCompanyName());
        
        return company;
    }
}
