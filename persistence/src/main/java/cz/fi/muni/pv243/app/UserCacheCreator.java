package cz.fi.muni.pv243.app;

import org.infinispan.cdi.embedded.ConfigureCache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;

import javax.enterprise.inject.Produces;

public class UserCacheCreator {

    @UserCache
    @ConfigureCache("users")
    @Produces
    public Configuration specialCacheCfg() {
        return new ConfigurationBuilder()
                .eviction()
                .strategy(EvictionStrategy.LRU)
                .maxEntries(10)
                .build();
    }
}

