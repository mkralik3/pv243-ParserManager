package cz.fi.muni.pv243.infinispan.store;

import cz.fi.muni.pv243.entity.User;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.UserStore;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@ApplicationScoped
@CachedStore
public class CachedUserStore implements UserStore {

    @Inject
    @DefaultCacheConfiguration
    private Cache<Long, User> userCache;

    @Inject
    @JPAStore
    private UserStore delegate;

    @Override
    public List<User> getAllUsers() {
        return delegate.getAllUsers();
    }

    @Override
    public User addUser(User user) {
        user = delegate.addUser(user);
        userCache.put(user.getId(), user);
        return user;
    }
}
