package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.formation.cdb.mapper.CompanyMapper;
import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Company_;

@Repository("companyDaoBean")
public class CompanyDaoImpl implements CompanyDao {
    static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);
    
    private static final String LIST_REQUEST = "SELECT id, name FROM company LIMIT ? OFFSET ?;";
    private static final String READ_REQUEST = "SELECT id, name FROM company WHERE id = ?;";
    private static final String FIND_BY_NAME_REQUEST = "SELECT id, name FROM company WHERE name = ?;";
    private static final String DELETE_COMPANY_REQUEST  = "DELETE FROM company WHERE id = ?;";
    private static final String COUNT_REQUEST  = "SELECT COUNT(company.id) FROM company;";

    private JdbcTemplate jdbcTemplate;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManagerFactory.createEntityManager();
        this.criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
    }

    @Autowired
    public CompanyDaoImpl(DataSource dataSource) {
        super();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Company> list(int offset, int nbToPrint) {
        LOGGER.debug("Listing companies from {} ({} per page)", offset, nbToPrint);

        CriteriaQuery<Company> criteria = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = criteria.from(Company.class);
        criteria.select(companyRoot);
        List<Company> companies = entityManager.createQuery(criteria)
                                                .setFirstResult(offset)
                                                .setMaxResults(nbToPrint)
                                                .getResultList();
        return companies;
    }

    @Override
    public Optional<Company> read(long companyId) {
        LOGGER.debug("Showing info from company n°{}", companyId);

        Optional<Company> optCompany = Optional.empty();

        CriteriaQuery<Company> criteria = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = criteria.from(Company.class);
        criteria.select(companyRoot);
        criteria.where(criteriaBuilder.equal(companyRoot.get(Company_.id), companyId));
        optCompany = Optional.of(entityManager.createQuery(criteria).getSingleResult());
        
        return optCompany;
    }

    @Override
    public Optional<Company> findByName(String companyName) {
        LOGGER.debug("Showing info from company {}", companyName);

        Optional<Company> optCompany = Optional.empty();

        CriteriaQuery<Company> criteria = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = criteria.from(Company.class);
        criteria.select(companyRoot);
        criteria.where(criteriaBuilder.equal(companyRoot.get(Company_.name), companyName));
        optCompany = Optional.of(entityManager.createQuery(criteria).getSingleResult());

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

        CriteriaQuery<Company> criteria = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = criteria.from(Company.class);
        criteria.select(companyRoot);
        List<Company> companies = entityManager.createQuery(criteria).getResultList();
        
        return companies.size();
    }
}
