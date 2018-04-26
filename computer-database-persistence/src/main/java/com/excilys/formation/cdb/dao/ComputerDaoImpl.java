package com.excilys.formation.cdb.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.mapper.ComputerMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.model.Computer_;

@Repository("computerDaoBean")
@EnableTransactionManagement
public class ComputerDaoImpl implements ComputerDao {

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerDaoImpl.class);

    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
        this.criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
    }

    private static final String REQUEST_SELECT_FROM_JOIN = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name as company_name FROM computer LEFT JOIN company ON company.id=computer.company_id ";

    private static final String CREATE_REQUEST  = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);";
    private static final String UPDATE_REQUEST  = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
    private static final String DELETE_REQUEST  = "DELETE FROM computer WHERE id = ?;";
    private static final String DELETE_COMPANY_REQUEST  = "DELETE FROM computer WHERE company_id = ?;";

    private static final String READ_REQUEST    = " WHERE computer.id = ?;";
    private static final String LIST_REQUEST = " LIMIT ? OFFSET ?;";

    private static final String COUNT_REQUEST   = "SELECT COUNT(computer.id) FROM computer;";
    private static final String COUNT_SEARCH_REQUEST   = "SELECT COUNT(computer.id) FROM computer LEFT JOIN company ON company.id=computer.company_id WHERE computer.name LIKE ? OR company.name LIKE ?;";

    private static final String DESC = " DESC";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ComputerMapper computerMapper;
    
    @Autowired
    public ComputerDaoImpl(DataSource dataSource) {
        super();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Computer create(Computer computer) {
        LOGGER.debug("Creating computer {}", computer);

        executeCreateRequest(computer);

        return computer;
    }

    private void executeCreateRequest(Computer computer) {
        Company company = computer.getCompany();
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                LocalDate intro;
                LocalDate discont;
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

        LOGGER.debug("Execution of the SQL query \"{}\" with parameters [{}, {}, {}, {}]", CREATE_REQUEST, computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), company == null ? null : company.getId());
        jdbcTemplate.update(psc, generatedKeyHolder);
        
        computer.setId(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public Optional<Computer> read(long computerId) {
        LOGGER.debug("Showing info from computer n°{}", computerId);

        Optional<Computer> optComputer = Optional.empty();

        CriteriaQuery<Computer> readQuery = criteriaBuilder.createQuery(Computer.class);
        Root<Computer> computerRoot = readQuery.from(Computer.class);
        readQuery.select(computerRoot);
        readQuery.where(criteriaBuilder.equal(computerRoot.get(Computer_.id), computerId));
        optComputer = Optional.of(entityManager.createQuery(readQuery).getSingleResult());

        return optComputer;
    }

    @Override
    public Computer update(Computer computer) {
        LOGGER.debug("Updating computer {}", computer);

        executeUpdateRequest(computer);

        return computer;
    }

    private void executeUpdateRequest(Computer computer) {
        Company company = computer.getCompany();
        Long companyId = company == null ? null : company.getId();

        String query = UPDATE_REQUEST;
        Object[] params = new Object[] {computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), companyId, computer.getId()};
        LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s) {}", query, params);
        jdbcTemplate.update(query, params);
    }

    @Override
    public void delete(long id) {
        LOGGER.debug("Deleting computer n°{}", id);

        jdbcTemplate.update(DELETE_REQUEST, id);
    }

    @Override
    @Transactional(rollbackFor=DaoException.class)
    public void deleteMany(List<Long> ids) {
        LOGGER.debug("Deleting computers n°{}", ids);

        for (long id : ids) {
            LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s) {}", DELETE_REQUEST, id);
            jdbcTemplate.update(DELETE_REQUEST, id);
            LOGGER.debug("Deletion of computer n°{} successful", id);
        }
    }

    @Override
    public void deleteByCompany(long companyId) {
        LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s) {}", DELETE_COMPANY_REQUEST, companyId);
        jdbcTemplate.update(DELETE_COMPANY_REQUEST, companyId);
    }

    @Override
    public List<Computer> list(int offset, int nbToPrint, String order, boolean desc) {
        LOGGER.debug("Listing computers from {} ({} per page) ordered by {}", offset, nbToPrint, order);

        return executeListRequest(offset, nbToPrint, order, desc);
    }

    private List<Computer> executeListRequest(int offset, int nbToPrint, String order, boolean desc) {
        String field = switchOrder(order, desc);
        StringBuilder req = new StringBuilder().append(REQUEST_SELECT_FROM_JOIN).append("ORDER BY ").append(field).append(LIST_REQUEST);
        Object[] params = new Object[] {nbToPrint, offset};

        LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s) {}", req.toString(), params);
        return jdbcTemplate.query(req.toString(), params, computerMapper);
    }

    @Override
    public List<Computer> listSearch(int offset, int nbToPrint, String order, boolean desc, String search) {
        LOGGER.debug("Listing search result from search \"{}\" beginning at {} ({} per page) ordered by {}", search, offset, nbToPrint, order);

        return executeListSearchRequest(offset, nbToPrint, order, desc, search);
    }

    private List<Computer> executeListSearchRequest(int offset, int nbToPrint, String order, boolean desc, String search) {
        String field = switchOrder(order, desc);
        StringBuilder req = new StringBuilder().append(REQUEST_SELECT_FROM_JOIN).append(" WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY ").append(field).append(LIST_REQUEST);

        Object[] params = new Object[] {search + "%", search + "%", nbToPrint, offset};
        LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s) {}", req.toString(), params);
        return jdbcTemplate.query(req.toString(), params, computerMapper);
    }
    
    private String switchOrder(String order, boolean desc) {
        String field;

        switch (ComputerOrderBy.parse(order)) {
            case ID: field = ComputerOrderBy.ID.toString() + (desc ? DESC : ""); break;
            case NAME: field = ComputerOrderBy.NAME.toString() + (desc ? DESC : ""); break;
            case INTRODUCED: field = ComputerOrderBy.INTRODUCED.toString() + (desc ? DESC : ""); break;
            case DISCONTINUED: field = ComputerOrderBy.DISCONTINUED.toString() + (desc ? DESC : ""); break;
            case COMPANY_NAME: field = ComputerOrderBy.COMPANY_NAME.toString() + (desc ? DESC : ""); break;
            default: field = ComputerOrderBy.ID.toString();
        }
        
        return field;
    }

    @Override
    public long count() {
        LOGGER.debug("Counting computers");

        LOGGER.debug("Execution of the SQL query \"{}\"", COUNT_REQUEST);
        return jdbcTemplate.queryForObject(COUNT_REQUEST, Long.class).longValue();
    }

    @Override
    public long countSearch(String search) {
        LOGGER.debug("Counting search results");

        Object[] params = new Object[] {search + "%", search + "%"};
        LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s)", COUNT_REQUEST, params);
        return jdbcTemplate.queryForObject(COUNT_SEARCH_REQUEST, params, Long.class).longValue();
    }
}
