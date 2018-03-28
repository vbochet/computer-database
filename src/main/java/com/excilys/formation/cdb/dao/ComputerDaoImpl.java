package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.persistence.ConnectionManager;

public enum ComputerDaoImpl implements ComputerDao {

    INSTANCE;

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerDaoImpl.class);

    private final String CREATE_REQUEST = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);";
    private final String READ_REQUEST   = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name as company_name FROM computer LEFT JOIN company ON company.id=computer.company_id WHERE computer.id = ?;";
    private final String UPDATE_REQUEST = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
    private final String DELETE_REQUEST = "DELETE FROM computer WHERE id = ?;";
    private final String LIST_REQUEST   = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name as company_name FROM computer LEFT JOIN company ON company.id=computer.company_id LIMIT ? OFFSET ?;";
    private final String COUNT_REQUEST  = "SELECT COUNT(computer.id) FROM computer;";

    @Override
    public Computer create(Computer computer) throws DaoException {
        LOGGER.info("Creating computer " + computer);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection.setAutoCommit(false);
            executeCreateRequest(connection, preparedStatement, resultSet, computer);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.error("SQL error in computer creation", e1);
                throw(new DaoException("SQL error in computer creation", e1));
            }

            LOGGER.error("SQL error in computer creation", e);
            throw(new DaoException("SQL error in computer creation", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return computer;
    }

    private void executeCreateRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, Computer computer) throws SQLException {
        Company company = computer.getCompany();
        LocalDate intro, discont;

        preparedStatement = connection.prepareStatement(CREATE_REQUEST, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, computer.getName());

        intro = computer.getIntroduced();
        if (intro == null) {
            preparedStatement.setNull(2, java.sql.Types.DATE);
        } else {
            preparedStatement.setDate(2, Date.valueOf(intro));
        }

        discont = computer.getDiscontinued();
        if (discont == null) {
            preparedStatement.setNull(3, java.sql.Types.DATE);
        } else {
            preparedStatement.setDate(3, Date.valueOf(discont));
        }

        if (company == null) {
            preparedStatement.setNull(4, java.sql.Types.BIGINT);
        } else {
            preparedStatement.setLong(4, company.getId());
        }

        preparedStatement.executeUpdate();

        resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.first()) {
            computer.setId(resultSet.getLong(1));
        }
    }

    @Override
    public Optional<Computer> read(long id) throws DaoException {
        LOGGER.info("Showing info from computer n°" + id);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Optional<Computer> optComputer = Optional.empty();

        try {
            optComputer = executeReadRequest(connection, preparedStatement, resultSet, id);
        } catch (SQLException e) {
            LOGGER.error("SQL error in computer reading", e);
            throw(new DaoException("SQL error in computer reading", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return optComputer;
    }

    private Optional<Computer> executeReadRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, long id) throws SQLException {
        Optional<Computer> optComputer = Optional.empty();

        preparedStatement = connection.prepareStatement(READ_REQUEST);
        preparedStatement.setLong(1, id);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.first()) {
            optComputer = Optional.of(ComputerMapper.INSTANCE.resultSetToComputer(resultSet));
        }

        return optComputer;
    }

    @Override
    public Computer update(Computer computer) throws DaoException {
        LOGGER.info("Updating computer " + computer);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection.setAutoCommit(false);
            executeUpdateRequest(connection, preparedStatement, resultSet, computer);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.error("SQL error in computer update", e1);
                throw(new DaoException("SQL error in computer update", e1));
            }

            LOGGER.error("SQL error in computer update", e);
            throw(new DaoException("SQL error in computer update", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return computer;
    }

    private int executeUpdateRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, Computer computer) throws SQLException {
        Company company = computer.getCompany();
        LocalDate intro, discont;

        preparedStatement = connection.prepareStatement(UPDATE_REQUEST);

        preparedStatement.setString(1, computer.getName());

        intro = computer.getIntroduced();
        if (intro == null) {
            preparedStatement.setNull(2, java.sql.Types.DATE);
        } else {
            preparedStatement.setDate(2, Date.valueOf(intro));
        }

        discont = computer.getDiscontinued();
        if (discont == null) {
            preparedStatement.setNull(3, java.sql.Types.DATE);
        } else {
            preparedStatement.setDate(3, Date.valueOf(discont));
        }

        if (company == null) {
            preparedStatement.setNull(4, java.sql.Types.BIGINT);
        } else {
            preparedStatement.setLong(4, company.getId());
        }

        preparedStatement.setLong(5, computer.getId());

        return preparedStatement.executeUpdate();
    }

    @Override
    public void delete(long id) throws DaoException {
        LOGGER.info("Deleting computer n°" + id);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);
            executeDeleteRequest(connection, preparedStatement, id);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.error("SQL error in companies listing", e1);
                throw(new DaoException("SQL error in companies listing", e1));
            }

            LOGGER.error("SQL error in computer deletion", e);
            throw(new DaoException("SQL error in computer deletion", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, null);
        }
    }

    @Override
    public void deleteMany(List<Long> ids) throws DaoException {
        LOGGER.info("Deleting computers n°" + ids);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);

            for (long id : ids) {
                    executeDeleteRequest(connection, preparedStatement, id);
                    LOGGER.info("Deletion of computers n°" + id);
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.error("SQL error in computer list deletion", e1);
                throw(new DaoException("SQL error in computer list deletion", e1));
            }

            LOGGER.error("SQL error in computer list deletion", e);
            throw(new DaoException("SQL error in computer list deletion", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, null);
        }
    }

    private void executeDeleteRequest(Connection connection, PreparedStatement preparedStatement, Long id) throws SQLException {
        preparedStatement = connection.prepareStatement(DELETE_REQUEST);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Computer> list(int offset, int nbToPrint) throws DaoException {
        LOGGER.info("Listing computers from " + offset + " (" + nbToPrint + " per page)");

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Computer> computersList = new ArrayList<>();

        try {
            executeListRequest(connection, preparedStatement, resultSet, offset, nbToPrint, computersList);
        } catch (SQLException e) {
            LOGGER.error("SQL error in computer listing", e);
            throw(new DaoException("SQL error in computer listing", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return computersList;
    }

    private void executeListRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, int offset, int nbToPrint, List<Computer> computersList) throws SQLException {
        preparedStatement = connection.prepareStatement(LIST_REQUEST);
        preparedStatement.setInt(1, nbToPrint);
        preparedStatement.setLong(2, offset);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            computersList.add(ComputerMapper.INSTANCE.resultSetToComputer(resultSet));
        }
    }

    @Override
    public long count() throws DaoException {
        LOGGER.info("Counting computers");

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
            LOGGER.error("SQL error in computer counting", e);
            throw(new DaoException("SQL error in computer counting", e));
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, statement, resultSet);
        }

        return count;
    }
}
