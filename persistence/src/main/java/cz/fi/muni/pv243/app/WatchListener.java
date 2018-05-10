package cz.fi.muni.pv243.app;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import cz.fi.muni.pv243.entity.User;

@Listener
public class WatchListener {

    @CacheEntryCreated
    public void entryCreated(CacheEntryCreatedEvent event) {
        if (!event.isPre()) {
            User user = (User) event.getValue();
            System.out.println("New user :" + user);
        }
    }
}
