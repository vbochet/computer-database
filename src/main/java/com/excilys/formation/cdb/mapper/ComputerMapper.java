package com.excilys.formation.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.formation.cdb.dto.ComputerDto;
import com.excilys.formation.cdb.exceptions.MapperException;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.service.CompanyService;

@Component
public class ComputerMapper {

    @Autowired
    CompanyService companyService;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerMapper.class);

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
        long companyId = -1;
        if (computer.getIntroduced() != null) {
            intro = computer.getIntroduced().toString();
        }
        if (computer.getDiscontinued() != null) {
            discont = computer.getDiscontinued().toString();
        }
        if (computer.getCompany() != null) {
            companyId = computer.getCompany().getId();
            companyName = computer.getCompany().getName();
        }
        

        computerDto.setComputerId(computer.getId());
        computerDto.setComputerName(computer.getName());
        computerDto.setComputerIntroduced(intro);
        computerDto.setComputerDiscontinued(discont);
        computerDto.setComputerCompanyId(companyId);
        computerDto.setComputerCompanyName(companyName);

        return computerDto;
    }

    public Computer computerDtoToComputer(ComputerDto computerDto) throws MapperException {
        Computer computer = new Computer();
        LocalDate intro = null, discont = null;
        
        try {
            intro = Date.valueOf(computerDto.getComputerIntroduced()).toLocalDate();
        } catch (IllegalArgumentException e) {
            // do nothing
        }
        
        try {
            discont = Date.valueOf(computerDto.getComputerDiscontinued()).toLocalDate();
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        computer.setId(computerDto.getComputerId());
        computer.setName(computerDto.getComputerName());
        computer.setIntroduced(intro);
        computer.setDiscontinued(discont);
        Optional<Company> optCompany;
        optCompany = companyService.getById(computerDto.getComputerCompanyId());
        if (optCompany.isPresent()) {
            computer.setCompany(optCompany.get());
        } else {
            computer.setCompany(null);
        }

        return computer;
}
}
