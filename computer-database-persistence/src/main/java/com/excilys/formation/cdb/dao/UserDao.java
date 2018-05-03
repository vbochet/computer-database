package com.excilys.formation.cdb.dao;

import java.util.List;
import java.util.Optional;

import com.excilys.formation.cdb.model.User;

public interface UserDao {

    User create(User user);
    Optional<User> getByUsername(String username);
    User update(User user);
    void delete(String username);
    List<User> list();

}
