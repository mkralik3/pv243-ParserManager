package cz.fi.muni.pv243.rest;


import cz.fi.muni.pv243.entity.User;
import cz.fi.muni.pv243.entity.UserManager;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/users")
public class UserService {

    @Inject
    private UserManager userManager;

    @GET
    @Produces("application/json")
    public List<User> showAllUsers() {
        return userManager.getAllUsers();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public User consumeJSON(User user) {
        userManager.addUser(user);
        return user;
    }
}