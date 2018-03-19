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
        Computer c = new Computer();
        Date intro = result.getDate("introduced");
        Date discont = result.getDate("discontinued");
        LocalDate ldIntro = intro == null ? null : intro.toLocalDate();
        LocalDate ldDiscont = discont == null ? null : discont.toLocalDate();

        c.setId(result.getLong("id"));
        c.setName(result.getString("name"));
        c.setIntroduced(ldIntro);
        c.setDiscontinued(ldDiscont);
        c.setCompany(new Company(result.getLong("company_id"), result.getString("company_name")));

        return c;
    }
}
