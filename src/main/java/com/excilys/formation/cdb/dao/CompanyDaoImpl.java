package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.mapper.RowCompanyMapper;
import com.excilys.formation.cdb.mapper.RowComputerMapper;
import com.excilys.formation.cdb.model.Company;

@Repository("companyDaoBean")
@EnableTransactionManagement
public class CompanyDaoImpl implements CompanyDao {
    static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);

    private DataSource dataSource;
    
    private final String LIST_REQUEST = "SELECT id, name FROM company LIMIT ? OFFSET ?;";
    private final String READ_REQUEST = "SELECT id, name FROM company WHERE id = ?;";
    private final String FIND_BY_NAME_REQUEST = "SELECT id, name FROM company WHERE name = ?;";
    private final String DELETE_COMPANY_REQUEST  = "DELETE FROM company WHERE id = ?;";
    private final String DELETE_COMPUTER_REQUEST  = "DELETE FROM computer WHERE company_id = ?;";
    private final String COUNT_REQUEST  = "SELECT COUNT(company.id) FROM company;";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyDaoImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private void DaoExceptionThrower(String errorMsg, Exception e) throws DaoException {
        LOGGER.error(errorMsg, e);
        throw(new DaoException(errorMsg, e));
    }

    @Override
    public List<Company> list(int offset, int nbToPrint) throws DaoException {
        LOGGER.debug("Listing companies from {} ({} per page)", offset, nbToPrint);

        List<Company> companiesList = null;

        try {
            String query = LIST_REQUEST;
            Object[] params = new Object[] {nbToPrint, offset};

            LOGGER.debug("Execution of the SQL query {} with arguments {}", query, params);
            companiesList =  jdbcTemplate.query(query, params, new RowCompanyMapper());
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in companies listing" ,e);
        }

        return companiesList;
    }

    @Override
    public Optional<Company> read(long companyId) throws DaoException {
        LOGGER.debug("Showing info from company n°{}", companyId);

        Optional<Company> optCompany = Optional.empty();

        try {
            String query = READ_REQUEST;
            Object[] params = new Object[] {companyId};
            LOGGER.debug("Execution of the SQL query {} with arguments {}", query, params);
            List<Company> companyList = jdbcTemplate.query(query, params, new RowCompanyMapper());
            if (companyList.size() == 1) {
                optCompany = Optional.of(companyList.get(0));
            }
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in company reading", e);
            LOGGER.error("SQL error in company reading\n", e);
        }

        return optCompany;
    }

    @Override
    public Optional<Company> findByName(String companyName) throws DaoException {
        LOGGER.debug("Showing info from company {}", companyName);

        Optional<Company> optCompany = Optional.empty();

        try {
            String query = FIND_BY_NAME_REQUEST;
            Object[] params = new Object[] {companyName};
            LOGGER.debug("Execution of the SQL query {} with arguments {}", query, params);
            List<Company> companyList = jdbcTemplate.query(query, params, new RowCompanyMapper());
            if (companyList.size() == 1) {
                optCompany = Optional.of(companyList.get(0));
            }
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in company reading", e);
            LOGGER.error("SQL error in company reading\n", e);
        }

        return optCompany;
    }

    @Override
    @Transactional(rollbackFor=DaoException.class)
    public void deleteById(long companyId) throws DaoException {
        LOGGER.debug("Deleting company n°{}", companyId);

        Connection connection = getConnection();

        try {
            executeDeleteComputerRequest(connection, companyId);
            executeDeleteCompanyRequest(connection, companyId);
        } catch (SQLException e) {
            String errorMsg = "SQL error in company deletion";
            DaoExceptionThrower(errorMsg, e);
        }
    }

    private void executeDeleteCompanyRequest(Connection connection, Long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMPANY_REQUEST);) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private void executeDeleteComputerRequest(Connection connection, Long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMPUTER_REQUEST);) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public long count() throws DaoException {
        LOGGER.debug("Counting companies");

        Connection connection = getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        long count = -1;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(COUNT_REQUEST);
            if(resultSet.next()) {
                count = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in companies counting", e);
        } finally {
            DaoUtils.closeElements(connection, statement, resultSet);
        }

        return count;
    }
    
    private Connection getConnection() throws DaoException {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
        } catch (CannotGetJdbcConnectionException e) {
            DaoExceptionThrower("Error while getting connection", e);
        }
        return connection;
    }
}
