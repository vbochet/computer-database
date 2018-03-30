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

    private final String REQUEST_SELECT_FROM_JOIN = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name as company_name FROM computer LEFT JOIN company ON company.id=computer.company_id ";
    
    private final String CREATE_REQUEST  = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);";
    private final String UPDATE_REQUEST  = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
    private final String DELETE_REQUEST  = "DELETE FROM computer WHERE id = ?;";
    
    private final String READ_REQUEST    = " WHERE computer.id = ?;";
    private final String LIST_REQUEST = " LIMIT ? OFFSET ?;";

    private final String COUNT_REQUEST   = "SELECT COUNT(computer.id) FROM computer;";
    private final String COUNT_SEARCH_REQUEST   = "SELECT COUNT(computer.id) FROM computer LEFT JOIN company ON company.id=computer.company_id WHERE computer.name LIKE ? OR company.name LIKE ?;";

    private final String DESC = " DESC";

    private void DaoExceptionThrower(String errorMsg, Exception e) throws DaoException {
        LOGGER.error(errorMsg, e);
        throw(new DaoException(errorMsg, e));
    }

    @Override
    public Computer create(Computer computer) throws DaoException {
        LOGGER.debug("Creating computer {}", computer);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection.setAutoCommit(false);
            executeCreateRequest(connection, preparedStatement, resultSet, computer);
            connection.commit();
        } catch (SQLException e) {
            String errorMsg = "SQL error in computer creation";
            try {
                connection.rollback();
            } catch (SQLException e1) {
                DaoExceptionThrower(errorMsg, e1);
            }

            DaoExceptionThrower(errorMsg, e);
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
        LOGGER.debug("Showing info from computer n°{}", id);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Optional<Computer> optComputer = Optional.empty();

        try {
            optComputer = executeReadRequest(connection, preparedStatement, resultSet, id);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in computer reading", e);
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return optComputer;
    }

    private Optional<Computer> executeReadRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, long id) throws SQLException {
        Optional<Computer> optComputer = Optional.empty();

        preparedStatement = connection.prepareStatement(REQUEST_SELECT_FROM_JOIN + READ_REQUEST);
        preparedStatement.setLong(1, id);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.first()) {
            optComputer = Optional.of(ComputerMapper.INSTANCE.resultSetToComputer(resultSet));
        }

        return optComputer;
    }

    @Override
    public Computer update(Computer computer) throws DaoException {
        LOGGER.debug("Updating computer {}", computer);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection.setAutoCommit(false);
            executeUpdateRequest(connection, preparedStatement, computer);
            connection.commit();
        } catch (SQLException e) {
            String errorMsg = "SQL error in computer update";
            try {
                connection.rollback();
            } catch (SQLException e1) {
                DaoExceptionThrower(errorMsg, e1);
            }

            DaoExceptionThrower(errorMsg, e);
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return computer;
    }

    private int executeUpdateRequest(Connection connection, PreparedStatement preparedStatement, Computer computer) throws SQLException {
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
        LOGGER.debug("Deleting computer n°{}", id);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);
            executeDeleteRequest(connection, preparedStatement, id);
            connection.commit();
        } catch (SQLException e) {
            String errorMsg = "SQL error in computer deletion";
            try {
                connection.rollback();
            } catch (SQLException e1) {
                DaoExceptionThrower(errorMsg, e1);
            }

            DaoExceptionThrower(errorMsg, e);
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, null);
        }
    }

    @Override
    public void deleteMany(List<Long> ids) throws DaoException {
        LOGGER.debug("Deleting computers n°{}", ids);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);

            for (long id : ids) {
                executeDeleteRequest(connection, preparedStatement, id);
                LOGGER.debug("Deletion of computers n°{}", id);
            }

            connection.commit();
        } catch (SQLException e) {
            String errorMsg = "SQL error in computer list deletion";
            try {
                connection.rollback();
            } catch (SQLException e1) {
                DaoExceptionThrower(errorMsg, e1);
            }

            DaoExceptionThrower(errorMsg, e);
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
    public List<Computer> list(int offset, int nbToPrint, String order, boolean desc) throws DaoException {
        LOGGER.debug("Listing computers from {} ({} per page) ordered by {}", offset, nbToPrint, order);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Computer> computersList = new ArrayList<>();

        try {
            executeListRequest(connection, preparedStatement, resultSet, offset, nbToPrint, order, desc, computersList);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in computer listing", e);
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return computersList;
    }

    private void executeListRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, int offset, int nbToPrint, String order, boolean desc, List<Computer> computersList) throws SQLException {
        String field;
        StringBuilder req = new StringBuilder();

        switch (ComputerOrderBy.parse(order)) {
            case ID: field = ComputerOrderBy.ID.toString() + (desc ? DESC : ""); break;
            case NAME: field = ComputerOrderBy.NAME.toString() + (desc ? DESC : ""); break;
            case INTRODUCED: field = ComputerOrderBy.INTRODUCED.toString() + (desc ? DESC : ""); break;
            case DISCONTINUED: field = ComputerOrderBy.DISCONTINUED.toString() + (desc ? DESC : ""); break;
            case COMPANY_NAME: field = ComputerOrderBy.COMPANY_NAME.toString() + (desc ? DESC : ""); break;
            default: field = ComputerOrderBy.ID.toString();
        }

        req.append(REQUEST_SELECT_FROM_JOIN).append("ORDER BY ").append(field).append(LIST_REQUEST);
        preparedStatement = connection.prepareStatement(req.toString());
        
        preparedStatement.setInt(1, nbToPrint);
        preparedStatement.setLong(2, offset);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            computersList.add(ComputerMapper.INSTANCE.resultSetToComputer(resultSet));
        }
    }

    @Override
    public List<Computer> listSearch(int offset, int nbToPrint, String order, boolean desc, String search) throws DaoException {
        LOGGER.debug("Listing search result from search \"{}\" beginning at {} ({} per page) ordered by {}", search, offset, nbToPrint, order);

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Computer> computersList = new ArrayList<>();

        try {
            executeListSearchRequest(connection, preparedStatement, resultSet, offset, nbToPrint, order, desc, search, computersList);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in search result listing", e);
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return computersList;
    }

    private void executeListSearchRequest(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, int offset, int nbToPrint, String order, boolean desc, String search, List<Computer> computersList) throws SQLException {
        String field;
        StringBuilder req;
        
        switch (ComputerOrderBy.parse(order)) {
            case ID: field = ComputerOrderBy.ID.toString() + (desc ? DESC : ""); break;
            case NAME: field = ComputerOrderBy.NAME.toString() + (desc ? DESC : ""); break;
            case INTRODUCED: field = ComputerOrderBy.INTRODUCED.toString() + (desc ? DESC : ""); break;
            case DISCONTINUED: field = ComputerOrderBy.DISCONTINUED.toString() + (desc ? DESC : ""); break;
            case COMPANY_NAME: field = ComputerOrderBy.COMPANY_NAME.toString() + (desc ? DESC : ""); break;
            default: field = ComputerOrderBy.ID.toString();
        }

        req.append(REQUEST_SELECT_FROM_JOIN).append(" WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY ").append(field).append(LIST_REQUEST);
        preparedStatement = connection.prepareStatement(req.toString());

        preparedStatement.setString(1, search + "%");
        preparedStatement.setString(2, search + "%");
        preparedStatement.setInt(3, nbToPrint);
        preparedStatement.setLong(4, offset);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            computersList.add(ComputerMapper.INSTANCE.resultSetToComputer(resultSet));
        }
    }

    @Override
    public long count() throws DaoException {
        LOGGER.debug("Counting computers");

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
            DaoExceptionThrower("SQL error in computer counting", e);
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, statement, resultSet);
        }

        return count;
    }

    public long countSearch(String search) throws DaoException {
        LOGGER.debug("Counting search results");

        Connection connection = ConnectionManager.INSTANCE.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        long count = -1;

        try {
            preparedStatement = connection.prepareStatement(COUNT_SEARCH_REQUEST);

            preparedStatement.setString(1, search + "%");
            preparedStatement.setString(2, search + "%");
            resultSet = preparedStatement.executeQuery();

            if(resultSet.first()) {
                count = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in search results counting", e);
        } finally {
            ConnectionManager.INSTANCE.closeElements(connection, preparedStatement, resultSet);
        }

        return count;
    }
}
