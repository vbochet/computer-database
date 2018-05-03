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
import org.springframework.stereotype.Repository;

import com.excilys.formation.cdb.model.User;
import com.excilys.formation.cdb.model.User_;

@Repository("userDaoBean")
public class UserDaoImpl implements UserDao {

    static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void init() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

	@Override
	public User create(User user) {
        LOGGER.debug("Creating user {}", user);

        entityManager.persist(user);
        entityManager.flush();

        return user;
	}

	@Override
	public Optional<User> getByUsername(String username) {
        LOGGER.debug("Showing info from user {}", username);

        Optional<User> optUser = Optional.empty();

        CriteriaQuery<User> readQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = readQuery.from(User.class);
        readQuery.select(userRoot);
        readQuery.where(criteriaBuilder.equal(userRoot.get(User_.username), username));
        optUser = Optional.of(entityManager.createQuery(readQuery).getSingleResult());

        return optUser;
	}

	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<User> list() {
		// TODO Auto-generated method stub
		return null;
	}

}
