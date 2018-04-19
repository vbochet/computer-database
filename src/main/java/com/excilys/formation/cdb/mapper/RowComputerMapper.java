package com.excilys.formation.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.formation.cdb.model.Computer;

@Component
public class RowComputerMapper implements RowMapper<Computer> {

    @Autowired
    private ComputerMapper computerMapper;
    
    static final Logger LOGGER = LoggerFactory.getLogger(RowComputerMapper.class);

    @Override
    public Computer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return computerMapper.resultSetToComputer(resultSet);
    }

}
