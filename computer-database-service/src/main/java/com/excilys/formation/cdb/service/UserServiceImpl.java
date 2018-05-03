package com.excilys.formation.cdb.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.formation.cdb.dao.UserDao;
import com.excilys.formation.cdb.model.User;

@Service("userServiceBean")
@EnableTransactionManagement
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.debug("Loading information concerning user {}", username);
        Optional<User> optUser = userDao.getByUsername(username);
        if (optUser.isPresent()) {
    		LOGGER.debug("User {} found", username);
        	return optUser.get();
        } else {
    		LOGGER.debug("User {} not found", username);
        	throw new UsernameNotFoundException(username);
        }
	}

	@Override
	@Transactional
	public User createUser(User user) {
		LOGGER.debug("Creating user {}", user);
		return userDao.create(user);
	}

	@Override
	public List<User> listUsers() {
		LOGGER.debug("Retrieving user list");
		return userDao.list();
	}

}
