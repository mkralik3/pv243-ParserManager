package cz.fi.muni.pv243.rest;


import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.store.RestaurantStore;

import javax.inject.Inject;
import javax.ws.rs.*;
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