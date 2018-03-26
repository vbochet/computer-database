package com.excilys.formation.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;

public enum ComputerMapper {

    INSTANCE;

    public Computer resultSetToComputer(ResultSet result) throws SQLException {
        Computer computer = new Computer();
        Date intro = result.getDate("introduced");
        Date discont = result.getDate("discontinued");
        LocalDate ldIntro = intro == null ? null : intro.toLocalDate();
        LocalDate ldDiscont = discont == null ? null : discont.toLocalDate();

        computer.setId(result.getLong("id"));
        computer.setName(result.getString("name"));
        computer.setIntroduced(ldIntro);
        computer.setDiscontinued(ldDiscont);
        computer.setCompany(new Company(result.getLong("company_id"), result.getString("company_name")));

        return computer;
    }
    
    public ComputerDto computerToComputerDto(Computer computer) {
        ComputerDto computerDto = new ComputerDto();
        String intro = null, discont = null;
        String companyName = null;
        if (computer.getIntroduced() != null) {
            intro = computer.getIntroduced().toString();
        }
        if (computer.getDiscontinued() != null) {
            discont = computer.getDiscontinued().toString();
        }
        if (computer.getCompany() != null) {
            companyName = computer.getCompany().getName();
        }
        

        computerDto.setComputerId(computer.getId());
        computerDto.setComputerName(computer.getName());
        computerDto.setComputerIntroduced(intro);
        computerDto.setComputerDiscontinued(discont);
        computerDto.setComputerCompany(companyName);

        return computerDto;
    }
    
    public Computer computerDtoToComputer(ComputerDto computerDto) {
        Computer computer = new Computer();
        LocalDate intro = null, discont = null;
        
        try {
            intro = Date.valueOf(computerDto.getComputerIntroduced()).toLocalDate();
        } catch (IllegalArgumentException e) { }
        
        try {
            discont = Date.valueOf(computerDto.getComputerDiscontinued()).toLocalDate();
        } catch (IllegalArgumentException e) { }

        computer.setId(computerDto.getComputerId());
        computer.setName(computerDto.getComputerName());
        computer.setIntroduced(intro);
        computer.setDiscontinued(discont);
        Optional<Company> optCompany = CompanyService.INSTANCE.getByName(computerDto.getComputerCompanyName());
        if (optCompany.isPresent()) {
            computer.setCompany(optCompany.get());
        } else {
            computer.setCompany(null);
        }

        return computer;
    }
}
