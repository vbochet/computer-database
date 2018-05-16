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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.formation.cdb.model.Company_;
import com.excilys.formation.cdb.model.Computer;
import com.excilys.formation.cdb.model.Computer_;

@Repository("computerDaoBean")
public class ComputerDaoImpl implements ComputerDao {

    static final Logger LOGGER = LoggerFactory.getLogger(ComputerDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void init() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public Computer create(Computer computer) {
        LOGGER.debug("Creating computer {}", computer);

        entityManager.persist(computer);
        entityManager.flush();

        return computer;
    }

    @Override
    public Optional<Computer> read(long computerId) {
        LOGGER.debug("Showing info from computer n°{}", computerId);

        Optional<Computer> optComputer = Optional.empty();

        CriteriaQuery<Computer> readQuery = criteriaBuilder.createQuery(Computer.class);
        Root<Computer> computerRoot = readQuery.from(Computer.class);
        readQuery.select(computerRoot);
        readQuery.where(criteriaBuilder.equal(computerRoot.get(Computer_.id), computerId));
        try {
            optComputer = Optional.of(entityManager.createQuery(readQuery).getSingleResult());
        } catch (NoResultException e) {
        	optComputer = Optional.empty();
        }

        return optComputer;
    }

    @Override
    public Computer update(Computer computer) {
        LOGGER.debug("Updating computer {}", computer);

        entityManager.merge(computer);

        return computer;
    }

    @Override
    public void delete(long computerId) {
        LOGGER.debug("Deleting computer n°{}", computerId);

        CriteriaDelete<Computer> deleteQuery = criteriaBuilder.createCriteriaDelete(Computer.class);
        Root<Computer> computerRoot = deleteQuery.from(Computer.class);
        deleteQuery.where(criteriaBuilder.equal(computerRoot.get(Computer_.id), computerId));
        entityManager.createQuery(deleteQuery).executeUpdate();
    }

    @Override
    public void deleteMany(List<Long> ids) {
        LOGGER.debug("Deleting computers n°{}", ids);

        CriteriaDelete<Computer> deleteQuery = criteriaBuilder.createCriteriaDelete(Computer.class);
        Root<Computer> rootComputer = deleteQuery.from(Computer.class);
        deleteQuery.where(rootComputer.get(Computer_.id).in(ids));
        entityManager.createQuery(deleteQuery).executeUpdate();
    }

    @Override
    public void deleteByCompany(long companyId) {
        LOGGER.debug("Deleting computers with company n°{}", companyId);
        CriteriaDelete<Computer> deleteQuery = criteriaBuilder.createCriteriaDelete(Computer.class);
        Root<Computer> computerRoot = deleteQuery.from(Computer.class);
        deleteQuery.where(criteriaBuilder.equal(computerRoot.get(Computer_.company).get(Company_.id), companyId));
        entityManager.createQuery(deleteQuery).executeUpdate();
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
