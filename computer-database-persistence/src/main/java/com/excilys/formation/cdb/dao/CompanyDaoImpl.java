package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.formation.cdb.model.Company;
import com.excilys.formation.cdb.model.Company_;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.model.Computer_;

@Repository("companyDaoBean")
public class CompanyDaoImpl implements CompanyDao {
    static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);

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
    public Company create(Company company) {
        LOGGER.debug("Creating company {}", company);

        entityManager.persist(company);
        entityManager.flush();

        return company;
    }

    @Override
    public Optional<Company> read(long companyId) {
        LOGGER.debug("Showing info from company n°{}", companyId);

        Optional<Company> optCompany = Optional.empty();

        CriteriaQuery<Company> readQuery = criteriaBuilder.createQuery(Company.class);
        Root<Company> companyRoot = readQuery.from(Company.class);
        readQuery.select(companyRoot);
        readQuery.where(criteriaBuilder.equal(companyRoot.get(Company_.id), companyId));
        try {
            optCompany = Optional.of(entityManager.createQuery(readQuery).getSingleResult());
        } catch (NoResultException e) {
            // don't change optCompany value
        }
        
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
        try {
            optCompany = Optional.of(entityManager.createQuery(findByNameQuery).getSingleResult());
        } catch (NoResultException e) {
            // don't change optCompany value
        }

        return optCompany;
    }

    @Override
    public Company update(Company company) {
        LOGGER.debug("Updating company {}", company);

        entityManager.merge(company);

        return company;
    }

    @Override
    public void deleteById(long companyId) {
        LOGGER.debug("Deleting company n°{}", companyId);

        CriteriaDelete<Computer> deleteComputerQuery = criteriaBuilder.createCriteriaDelete(Computer.class);
        Root<Computer> rootComputer = deleteComputerQuery.from(Computer.class);
        deleteComputerQuery.where(criteriaBuilder.equal(rootComputer.get(Computer_.company), companyId));
        entityManager.createQuery(deleteComputerQuery).executeUpdate();

        CriteriaDelete<Company> deleteCompanyQuery = criteriaBuilder.createCriteriaDelete(Company.class);
        Root<Company> rootCompany = deleteCompanyQuery.from(Company.class);
        deleteCompanyQuery.where(criteriaBuilder.equal(rootCompany.get(Company_.id), companyId));
        entityManager.createQuery(deleteCompanyQuery).executeUpdate();
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
