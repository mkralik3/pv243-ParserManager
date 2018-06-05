package cz.fi.muni.pv243.store;

import cz.fi.muni.pv243.entity.User;

import java.util.List;

public interface UserStore {

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    User findById(Long id);

    List<User> getAllUsers();

}