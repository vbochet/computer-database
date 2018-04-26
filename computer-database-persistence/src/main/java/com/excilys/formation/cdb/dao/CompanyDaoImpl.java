package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
    
    private static final String DELETE_COMPANY_REQUEST  = "DELETE FROM company WHERE id = ?;";

    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void init() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public List<Company> list(int offset, int nbToPrint) {
        LOGGER.debug("Listing companies from {} ({} per page)", offset, nbToPrint);

        CriteriaQuery<Company> listQuery = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = listQuery.from(Company.class);
        listQuery.select(companyRoot);
        List<Company> companies = entityManager.createQuery(listQuery)
                                               .setFirstResult(offset)
                                               .setMaxResults(nbToPrint)
                                               .getResultList();

        return companies;
    }

    @Override
    public Optional<Company> read(long companyId) {
        LOGGER.debug("Showing info from company n°{}", companyId);

        Optional<Company> optCompany = Optional.empty();

        CriteriaQuery<Company> readQuery = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = readQuery.from(Company.class);
        readQuery.select(companyRoot);
        readQuery.where(criteriaBuilder.equal(companyRoot.get(Company_.id), companyId));
        optCompany = Optional.of(entityManager.createQuery(readQuery).getSingleResult());
        
        return optCompany;
    }

    @Override
    public Optional<Company> findByName(String companyName) {
        LOGGER.debug("Showing info from company {}", companyName);

        Optional<Company> optCompany = Optional.empty();

        CriteriaQuery<Company> findByNameQuery = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = findByNameQuery.from(Company.class);
        findByNameQuery.select(companyRoot);
        findByNameQuery.where(criteriaBuilder.equal(companyRoot.get(Company_.name), companyName));
        optCompany = Optional.of(entityManager.createQuery(findByNameQuery).getSingleResult());

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
        
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Company.class)));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return count;
    }
}
