package cz.fi.muni.pv243.store;

import cz.fi.muni.pv243.entity.User;

import java.util.List;

public interface UserStore {

    List<User> getAllUsers();

    User addUser(User user);
}
