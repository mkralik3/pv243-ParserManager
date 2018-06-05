package cz.fi.muni.pv243.infinispan.store;

import cz.fi.muni.pv243.entity.User;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.UserStore;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
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
    @Transactional
    public User addUser(User user) {
        user = delegate.addUser(user);
        userCache.put(user.getId(), user);
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        user = delegate.updateUser(user);
        userCache.put(user.getId(), user);
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        delegate.deleteUser(user);
        userCache.remove(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return delegate.getAllUsers();
    }

    @Override
    public User findById(Long id) {
        return userCache.computeIfAbsent(id,
                s -> delegate.findById(s));
    }
}
