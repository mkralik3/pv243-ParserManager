package cz.fi.muni.pv243.entity;

import cz.fi.muni.pv243.app.UserCache;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.stream.CacheCollectors;
import cz.fi.muni.pv243.app.WatchListener;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class UserManager {
/*
    private static final String CACHE_NAME = "users";

    private EmbeddedCacheManager cacheManager;
*/
    @Inject
    @UserCache
    private Cache<String, User> userCache;

    public UserManager() {
    }

    /*
    public void init() {
        GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
        global.transport().clusterName("InfinispanApp")
                .globalJmxStatistics()
                .allowDuplicateDomains(Boolean.TRUE);
        cacheManager = new DefaultCacheManager(global.build());
        cacheManager.addListener(new WatchListener());
        ConfigurationBuilder config = new ConfigurationBuilder();
        config
                .clustering().cacheMode(CacheMode.DIST_SYNC)
                .persistence().addSingleFileStore().location("/tmp/myDataStore");
        cacheManager.defineConfiguration(CACHE_NAME, config.build());
        userCache = cacheManager.getCache(CACHE_NAME);
    }*/

    public List<User> getAllUsers() {
        return userCache.values().stream().collect(CacheCollectors.serializableCollector(Collectors::toList));
    }


    public void addUser(User user){
        userCache.put(user.getName(),user);
    }
}
