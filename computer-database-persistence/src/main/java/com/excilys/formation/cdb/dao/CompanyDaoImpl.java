package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;

@Repository("companyDaoBean")
public class CompanyDaoImpl implements CompanyDao {
    static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);
    
    private static final String LIST_REQUEST = "SELECT id, name FROM company LIMIT ? OFFSET ?;";
    private static final String READ_REQUEST = "SELECT id, name FROM company WHERE id = ?;";
    private static final String FIND_BY_NAME_REQUEST = "SELECT id, name FROM company WHERE name = ?;";
    private static final String DELETE_COMPANY_REQUEST  = "DELETE FROM company WHERE id = ?;";
    private static final String COUNT_REQUEST  = "SELECT COUNT(company.id) FROM company;";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyDaoImpl(DataSource dataSource) {
        super();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Company> list(int offset, int nbToPrint) {
        LOGGER.debug("Listing companies from {} ({} per page)", offset, nbToPrint);

        String query = LIST_REQUEST;
        Object[] params = new Object[] {nbToPrint, offset};

        LOGGER.debug("Execution of the SQL query {} with parameter(s) {}", query, params);
        return jdbcTemplate.query(query, params, new CompanyMapper());
    }

    @Override
    public Optional<Company> read(long companyId) {
        LOGGER.debug("Showing info from company n°{}", companyId);

        Optional<Company> optCompany = Optional.empty();

        String query = READ_REQUEST;
        Object[] params = new Object[] {companyId};
        LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s) {}", query, params);
        List<Company> companyList = jdbcTemplate.query(query, params, new CompanyMapper());
        if (companyList.size() == 1) {
            optCompany = Optional.of(companyList.get(0));
        }

        return optCompany;
    }

    @Override
    public Optional<Company> findByName(String companyName) {
        LOGGER.debug("Showing info from company {}", companyName);

        Optional<Company> optCompany = Optional.empty();

        String query = FIND_BY_NAME_REQUEST;
        Object[] params = new Object[] {companyName};
        LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s) {}", query, params);
        List<Company> companyList = jdbcTemplate.query(query, params, new CompanyMapper());
        if (companyList.size() == 1) {
            optCompany = Optional.of(companyList.get(0));
        }

        return optCompany;
    }

    @Override
    public void deleteById(long companyId) {
        LOGGER.debug("Deleting company n°{}", companyId);

        LOGGER.debug("Execution of the SQL query \"{}\" with parameter(s) {}", DELETE_COMPANY_REQUEST, companyId);
        jdbcTemplate.update(DELETE_COMPANY_REQUEST, companyId);
    }

    @Override
    public long count() {
        LOGGER.debug("Counting companies");

        LOGGER.debug("Execution of the SQL query \"{}\"", COUNT_REQUEST);
        return jdbcTemplate.queryForObject(COUNT_REQUEST, Long.class).longValue();
    }
}
