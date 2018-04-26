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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
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

    private static final String CREATE_REQUEST  = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);";
    private static final String UPDATE_REQUEST  = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";
    private static final String DELETE_REQUEST  = "DELETE FROM computer WHERE id = ?;";
    private static final String DELETE_COMPANY_REQUEST  = "DELETE FROM computer WHERE company_id = ?;";


    private JdbcTemplate jdbcTemplate;
    
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

        CriteriaQuery<Computer> listQuery = criteriaBuilder.createQuery(Computer.class);
        Root<Computer> computerRoot = listQuery.from(Computer.class);
        listQuery.select(computerRoot);
        listQuery.orderBy(switchOrder(order, desc, computerRoot));
        List<Computer> computers = entityManager.createQuery(listQuery)
                                                .setFirstResult(offset)
                                                .setMaxResults(nbToPrint)
                                                .getResultList();

        return computers;
    }

    @Override
    public List<Computer> listSearch(int offset, int nbToPrint, String order, boolean desc, String search) {
        LOGGER.debug("Listing search result from search \"{}\" beginning at {} ({} per page) ordered by {}", search, offset, nbToPrint, order);

        CriteriaQuery<Computer> listQuery = criteriaBuilder.createQuery(Computer.class);
        Root<Computer> computerRoot = listQuery.from(Computer.class);
        listQuery.select(computerRoot);
        listQuery.orderBy(switchOrder(order, desc, computerRoot));
        Predicate computerNameCondition = criteriaBuilder.like(computerRoot.get("name"), "%" + search + "%");
        Predicate companyNameCondition = criteriaBuilder.like(computerRoot.get("company").get("name"), "%" + search + "%");
        listQuery.where(criteriaBuilder.or(computerNameCondition, companyNameCondition));
        List<Computer> computers = entityManager.createQuery(listQuery)
                                                .setFirstResult(offset)
                                                .setMaxResults(nbToPrint)
                                                .getResultList();

        return computers;
    }
    
    private Order switchOrder(String order, boolean desc, Root<Computer> computerRoot) {
        String field;
        Order condition;

        switch (ComputerOrderBy.parse(order)) {
            case ID: field = ComputerOrderBy.ID.toString(); break;
            case NAME: field = ComputerOrderBy.NAME.toString(); break;
            case INTRODUCED: field = ComputerOrderBy.INTRODUCED.toString(); break;
            case DISCONTINUED: field = ComputerOrderBy.DISCONTINUED.toString(); break;
            case COMPANY: field = ComputerOrderBy.COMPANY.toString(); break;
            default: field = ComputerOrderBy.ID.toString();
        }

        if (field.equals(ComputerOrderBy.COMPANY.toString())) {
            condition = desc ? criteriaBuilder.desc(computerRoot.get(field).get("name")) : criteriaBuilder.asc(computerRoot.get(field).get("name"));
        }
        else {
        	condition = desc ? criteriaBuilder.desc(computerRoot.get(field)) : criteriaBuilder.asc(computerRoot.get(field));
        }

        return condition;
    }

    @Override
    public long count() {
        LOGGER.debug("Counting computers");

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Computer.class)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return count;
    }

    @Override
    public long countSearch(String search) {
        LOGGER.debug("Counting search results");

        CriteriaQuery<Long> countSearchQuery = criteriaBuilder.createQuery(Long.class);
        Root<Computer> computerRoot = countSearchQuery.from(Computer.class);
        countSearchQuery.select(criteriaBuilder.count(computerRoot));
        Predicate computerNameCondition = criteriaBuilder.like(computerRoot.get("name"), "%" + search + "%");
        Predicate companyNameCondition = criteriaBuilder.like(computerRoot.get("company").get("name"), "%" + search + "%");
        countSearchQuery.where(criteriaBuilder.or(computerNameCondition, companyNameCondition));
        Long count = entityManager.createQuery(countSearchQuery).getSingleResult();

        return count;
    }
}
