package cz.fi.muni.pv243.rest.app;

import cz.fi.muni.pv243.rest.UserService;

import java.util.HashSet;
import java.util.Set;

public class RestApplication extends javax.ws.rs.core.Application {
    private Set<Object> singletons = new HashSet<Object>();

    public RestApplication() {
        singletons.add(new UserService());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
