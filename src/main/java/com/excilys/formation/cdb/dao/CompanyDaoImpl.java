package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.persistence.ConnectionManager;

public enum CompanyDaoImpl implements CompanyDao {

    INSTANCE;

    static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);

    private final String LIST_REQUEST = "SELECT id, name FROM company LIMIT ? OFFSET ?;";
    private final String READ_REQUEST = "SELECT id, name FROM company WHERE id = ?;";
    private final String FIND_BY_NAME_REQUEST = "SELECT id, name FROM company WHERE name = ?;";
    private final String COUNT_REQUEST  = "SELECT COUNT(company.id) FROM company;";

    @Override
    public List<Company> list(int offset, int nbToPrint) throws DaoException {
        LOGGER.info("Listing companies from " + offset + " (" + nbToPrint + " per page)");

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Company> companiesList = new ArrayList<>();

        try {
            executeListRequest(connection, preparedStatement, resultSet, offset, nbToPrint, companiesList);
        } catch (SQLException e) {
            LOGGER.error("SQL error in companies listing", e);
            throw(new DaoException("SQL error in companies listing", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return companiesList;
    }

    private void executeListRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, int offset, int nbToPrint, List<Company> companiesList) throws SQLException {
        preparedStatement = connection.prepareStatement(LIST_REQUEST);
        preparedStatement.setInt(1, nbToPrint);
        preparedStatement.setInt(2, offset);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            companiesList.add(CompanyMapper.INSTANCE.resultSetToCompany(resultSet));
        }
    }

    @Override
    public Optional<Company> read(long companyId) throws DaoException {
        LOGGER.info("Showing info from company n°" + companyId);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Optional<Company> optCompany = Optional.empty();

        try {
            optCompany = executeReadRequest(connection, preparedStatement, resultSet, companyId);
        } catch (SQLException e) {
            LOGGER.error("SQL error in company reading", e);
            throw(new DaoException("SQL error in companies reading", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return optCompany;
    };

    private Optional<Company> executeReadRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, long id) throws SQLException {
        preparedStatement = connection.prepareStatement(READ_REQUEST);
        preparedStatement.setLong(1, id);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.first()) {
            return Optional.of(CompanyMapper.INSTANCE.resultSetToCompany(resultSet));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Company> findByName(String companyName) throws DaoException {
        LOGGER.info("Showing info from company " + companyName);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Optional<Company> optCompany = Optional.empty();

        try {
            optCompany = executeFindByNameRequest(connection, preparedStatement, resultSet, companyName);
        } catch (SQLException e) {
            LOGGER.error("SQL error in company reading", e);
            throw(new DaoException("SQL error in company reading", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return optCompany;
    }

    private Optional<Company> executeFindByNameRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, String name) throws SQLException {
        preparedStatement = connection.prepareStatement(FIND_BY_NAME_REQUEST);
        preparedStatement.setString(1, name);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.first()) {
            return Optional.of(CompanyMapper.INSTANCE.resultSetToCompany(resultSet));
        }

        return Optional.empty();
    }

    @Override
    public long count() throws DaoException {
        LOGGER.info("Counting companies");

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        long count = -1;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(COUNT_REQUEST);
            if(resultSet.first()) {
                count = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL error in companies counting", e);
            throw(new DaoException("SQL error in companies counting", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, statement, resultSet);
        }

        return count;
    }
}
