package cz.fi.muni.pv243.rest;


import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.store.RestaurantStore;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/restaurants")
public class RestaurantService {

    @Inject
    @CachedStore
    private RestaurantStore restaurantStore;

    @GET
    @Produces("application/json")
    public List<Restaurant> getAllRestaurants() {
        return restaurantStore.getAllRestaurants();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Restaurant createRestaurant(Restaurant restaurant) {
        restaurantStore.addRestaurant(restaurant);
        return restaurant;
    }
}