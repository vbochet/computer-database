package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.formation.cdb.exceptions.DaoException;
import com.excilys.formation.cdb.mapper.RowCompanyMapper;
import com.excilys.formation.cdb.model.Company;

@Repository("companyDaoBean")
@EnableTransactionManagement
public class CompanyDaoImpl implements CompanyDao {
    static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);
    
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

        try {
            jdbcTemplate.update(DELETE_COMPUTER_REQUEST, companyId);
            jdbcTemplate.update(DELETE_COMPANY_REQUEST, companyId);
        } catch (DataAccessException e) {
            String errorMsg = "SQL error in company deletion";
            DaoExceptionThrower(errorMsg, e);
        }
    }

    @Override
    public long count() throws DaoException {
        LOGGER.debug("Counting companies");

        long count = -1;

        try {
            count = jdbcTemplate.queryForObject(COUNT_REQUEST, Long.class).longValue();
        } catch (DataAccessException e) {
            DaoExceptionThrower("SQL error in companies counting", e);
        }

        return count;
    }
}
