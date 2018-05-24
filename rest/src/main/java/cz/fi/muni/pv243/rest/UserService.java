package cz.fi.muni.pv243.rest;


import cz.fi.muni.pv243.entity.User;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.store.UserStore;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/users")
public class UserService {

    @Inject
    @CachedStore
    private UserStore userStore;

    @GET
    @Produces("application/json")
    public List<User> getAllUsers() {
        return userStore.getAllUsers();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public User createUser(User user) {
        userStore.addUser(user);
        return user;
    }
}