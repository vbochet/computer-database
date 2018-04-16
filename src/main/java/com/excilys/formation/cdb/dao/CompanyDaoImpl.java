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
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.mapper.CompanyMapper;
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

    @Autowired
    public CompanyDaoImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    private void DaoExceptionThrower(String errorMsg, Exception e) throws DaoException {
        LOGGER.error(errorMsg, e);
        throw(new DaoException(errorMsg, e));
    }

    @Override
    public List<Company> list(int offset, int nbToPrint) throws DaoException {
        LOGGER.debug("Listing companies from {} ({} per page)", offset, nbToPrint);

        Connection connection = getConnection();
        List<Company> companiesList = new ArrayList<>();

        try {
            executeListRequest(connection, offset, nbToPrint, companiesList);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in companies listing" ,e);
        } finally {
            DaoUtils.closeConnection(connection);
        }

        return companiesList;
    }

    private void executeListRequest(Connection connection, int offset, int nbToPrint, List<Company> companiesList) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(LIST_REQUEST);) {
            preparedStatement.setInt(1, nbToPrint);
            preparedStatement.setInt(2, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    companiesList.add(CompanyMapper.INSTANCE.resultSetToCompany(resultSet));
                }
            }
        }
    }

    @Override
    public Optional<Company> read(long companyId) throws DaoException {
        LOGGER.debug("Showing info from company n°{}", companyId);

        Connection connection = getConnection();
        Optional<Company> optCompany = Optional.empty();

        try {
            optCompany = executeReadRequest(connection, companyId);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in company reading", e);
            LOGGER.error("SQL error in company reading\n", e);
        } finally {
            DaoUtils.closeConnection(connection);
        }

        return optCompany;
    }

    private Optional<Company> executeReadRequest(Connection connection, long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(READ_REQUEST);) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()) {
                    LOGGER.error("Found company matching id {}\n", id);
                    return Optional.of(CompanyMapper.INSTANCE.resultSetToCompany(resultSet));
                }
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Company> findByName(String companyName) throws DaoException {
        LOGGER.debug("Showing info from company {}", companyName);

        Connection connection = getConnection();
        Optional<Company> optCompany = Optional.empty();

        try {
            optCompany = executeFindByNameRequest(connection, companyName);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in company reading", e);
            LOGGER.error("SQL error in company reading\n", e);
        } finally {
            DaoUtils.closeConnection(connection);
        }

        return optCompany;
    }

    private Optional<Company> executeFindByNameRequest(Connection connection, String name) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_REQUEST);) {
            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()) {
                    return Optional.of(CompanyMapper.INSTANCE.resultSetToCompany(resultSet));
                }
            }
            return Optional.empty();
        }
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
