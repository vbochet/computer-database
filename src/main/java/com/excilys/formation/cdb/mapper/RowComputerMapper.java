package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.excilys.formation.cdb.model.Computer;

public class RowComputerMapper implements RowMapper<Computer> {

    static final Logger LOGGER = LoggerFactory.getLogger(RowComputerMapper.class);

    @Override
    public Computer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return ComputerMapper.INSTANCE.resultSetToComputer(resultSet);
    }

}
