package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.mapper.RowComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;

@Repository("computerDaoBean")
@EnableTransactionManagement
public class ComputerDaoImpl implements ComputerDao {

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

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ComputerDaoImpl(DataSource dataSource) {
        super();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private void DaoExceptionThrower(String errorMsg, Exception e) throws DaoException {
        LOGGER.error(errorMsg, e);
        throw(new DaoException(errorMsg, e));
    }

    @Override
    public Computer create(Computer computer) throws DaoException {
        LOGGER.debug("Creating computer {}", computer);


        try {
            executeCreateRequest(computer);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in computer creation", e);
        }

        return computer;
    }

    private void executeCreateRequest(Computer computer) throws SQLException {
        Company company = computer.getCompany();
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                LocalDate intro, discont;
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_REQUEST, Statement.RETURN_GENERATED_KEYS);

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
                
                return preparedStatement;
            }
        };

        LOGGER.debug("Execution of the PreparedStatementCreator {}", psc.toString());
        jdbcTemplate.update(psc, generatedKeyHolder);
        
        computer.setId(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public Optional<Computer> read(long id) throws DaoException {
        LOGGER.debug("Showing info from computer n°{}", id);

        Optional<Computer> optComputer = Optional.empty();

        try {
            String query = REQUEST_SELECT_FROM_JOIN + READ_REQUEST;
            Object[] params = new Object[] {id};
            LOGGER.debug("Execution of the SQL query {} with arguments {}", query, params);
            List<Computer> computerList = jdbcTemplate.query(query, params, new RowComputerMapper());
            if (computerList.size() == 1) {
                optComputer = Optional.of(computerList.get(0));
            }
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in computer reading", e);
        }

        return optComputer;
    }

    @Override
    public Computer update(Computer computer) throws DaoException {
        LOGGER.debug("Updating computer {}", computer);

        try {
            executeUpdateRequest(computer);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in computer update", e);
        }

        return computer;
    }

    private void executeUpdateRequest(Computer computer) throws SQLException {
        Company company = computer.getCompany();
        Long companyId = company == null ? null : company.getId();

        String query = UPDATE_REQUEST;
        Object[] params = new Object[] {computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), companyId, computer.getId()};
        LOGGER.debug("Execution of the SQL query {} with arguments {}", query, params);
        jdbcTemplate.update(query, params);
    }

    @Override
    public void delete(long id) throws DaoException {
        LOGGER.debug("Deleting computer n°{}", id);

        try {
            jdbcTemplate.update(DELETE_REQUEST, id);
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in computer deletion", e);
        }
    }

    @Override
    @Transactional(rollbackFor=DaoException.class)
    public void deleteMany(List<Long> ids) throws DaoException {
        LOGGER.debug("Deleting computers n°{}", ids);

        try {
            for (long id : ids) {
                jdbcTemplate.update(DELETE_REQUEST, id);
                LOGGER.debug("Deletion of computers n°{}", id);
            }
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in computer list deletion", e);
        }
    }

    @Override
    public List<Computer> list(int offset, int nbToPrint, String order, boolean desc) throws DaoException {
        LOGGER.debug("Listing computers from {} ({} per page) ordered by {}", offset, nbToPrint, order);

        List<Computer> computersList = null;

        try {
            computersList = executeListRequest(offset, nbToPrint, order, desc);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in computer listing", e);
        }

        return computersList;
    }

    private List<Computer> executeListRequest(int offset, int nbToPrint, String order, boolean desc) throws SQLException {
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
        Object[] params = new Object[] {nbToPrint, offset};

        LOGGER.debug("Execution of the SQL query {} with arguments {}", req.toString(), params);
        return jdbcTemplate.query(req.toString(), params, new RowComputerMapper());
    }

    @Override
    public List<Computer> listSearch(int offset, int nbToPrint, String order, boolean desc, String search) throws DaoException {
        LOGGER.debug("Listing search result from search \"{}\" beginning at {} ({} per page) ordered by {}", search, offset, nbToPrint, order);

        List<Computer> computersList = null;

        try {
            computersList = executeListSearchRequest(offset, nbToPrint, order, desc, search);
        } catch (SQLException e) {
            DaoExceptionThrower("SQL error in search result listing", e);
        }

        return computersList;
    }

    private List<Computer> executeListSearchRequest(int offset, int nbToPrint, String order, boolean desc, String search) throws SQLException {
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

        req.append(REQUEST_SELECT_FROM_JOIN).append(" WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY ").append(field).append(LIST_REQUEST);

        Object[] params = new Object[] {search + "%", search + "%", nbToPrint, offset};
        LOGGER.debug("Execution of the SQL query {} with arguments {}", req.toString(), params);
        return jdbcTemplate.query(req.toString(), params, new RowComputerMapper());
    }

    @Override
    public long count() throws DaoException {
        LOGGER.debug("Counting computers");

        long count = -1;

        try {
            count = jdbcTemplate.queryForObject(COUNT_REQUEST, Long.class).longValue();
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in computer counting", e);
        }

        return count;
    }

    @Override
    public long countSearch(String search) throws DaoException {
        LOGGER.debug("Counting search results");

        long count = -1;

        try {
            Object[] params = new Object[] {search + "%", search + "%"};
            count = jdbcTemplate.queryForObject(COUNT_SEARCH_REQUEST, params, Long.class).longValue();
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in search results counting", e);
        }

        return count;
    }
}
