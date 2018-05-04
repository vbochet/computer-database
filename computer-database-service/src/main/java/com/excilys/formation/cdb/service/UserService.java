package com.excilys.formation.cdb.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.excilys.formation.cdb.model.User;

public interface UserService extends UserDetailsService {

	User createUser(User user);
	User updateUser(User user);
	void deleteUser(String username);

	List<User> listUsers();

}
