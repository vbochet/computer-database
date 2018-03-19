package com.excilys.formation.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;

public enum ComputerMapper {

    INSTANCE;

    public Computer createComputer(ResultSet result) throws SQLException {
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
}
