package cz.fi.muni.pv243.infinispan.store;

import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.RestaurantStore;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;

@Named
@ApplicationScoped
@CachedStore
public class CachedRestaurantStore implements RestaurantStore {

    @Inject
    @JPAStore
    private RestaurantStore delegate;

    @Inject
    @DefaultCacheConfiguration
    private Cache<String, Restaurant> restaurantCache;

    @Override
    @Transactional
    public Restaurant addRestaurant(Restaurant restaurant){
        restaurant = delegate.addRestaurant(restaurant);
        restaurantCache.put(restaurant.getGooglePlaceID(), restaurant);
        return restaurant;
    }

    @Override
    @Transactional
    public Restaurant updateRestaurant(Restaurant restaurant) {
        restaurant = delegate.updateRestaurant(restaurant);
        restaurantCache.put(restaurant.getGooglePlaceID(), restaurant);
        return restaurant;
    }

    @Override
    @Transactional
    public void deleteRestaurant(Restaurant restaurant) {
        delegate.deleteRestaurant(restaurant);
        restaurantCache.remove(restaurant.getGooglePlaceID());
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return delegate.getAllRestaurants();
    }

    @Override
    public Restaurant findById(String googleID) {
        return restaurantCache.computeIfAbsent(googleID,
                s -> delegate.findById(s));
    }
}