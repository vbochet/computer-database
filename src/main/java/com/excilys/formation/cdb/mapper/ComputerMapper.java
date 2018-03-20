package com.excilys.formation.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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

        computerDto.setComputerId(computer.getId());
        computerDto.setComputerName(computer.getName());
        computerDto.setComputerIntroduced(computer.getIntroduced().toString());
        computerDto.setComputerDiscontinued(computer.getDiscontinued().toString());
        computerDto.setComputerCompany(computer.getCompany().getName());

        return computerDto;
    }
    
    public Computer computerDtoToComputer(ComputerDto computerDto) {
        Computer computer = new Computer();
        
        Date intro = Date.valueOf(computerDto.getComputerIntroduced());
        Date discont = Date.valueOf(computerDto.getComputerDiscontinued());
        LocalDate ldIntro = intro == null ? null : intro.toLocalDate();
        LocalDate ldDiscont = discont == null ? null : discont.toLocalDate();

        computer.setId(computerDto.getComputerId());
        computer.setName(computerDto.getComputerName());
        computer.setIntroduced(ldIntro);
        computer.setDiscontinued(ldDiscont);
        computer.setCompany(CompanyService.INSTANCE.getByName(computerDto.getComputerCompanyName()));

        return computer;
    }
}
