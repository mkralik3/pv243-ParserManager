package cz.fi.muni.pv243.store;

import cz.fi.muni.pv243.entity.Restaurant;

import java.util.List;

public interface RestaurantStore {

    List<Restaurant> getAllRestaurants();

    Restaurant addRestaurant(Restaurant parser);
}
