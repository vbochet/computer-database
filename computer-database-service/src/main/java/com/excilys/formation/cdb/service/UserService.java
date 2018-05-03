package com.excilys.formation.cdb.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.formation.cdb.dao.UserDao;
import com.excilys.formation.cdb.model.User;

@Service("userServiceBean")
@EnableTransactionManagement
public class UserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

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

}
