package cz.fi.muni.pv243.store;

import cz.fi.muni.pv243.entity.Restaurant;

import java.util.List;

public interface RestaurantStore {

    Restaurant addRestaurant(Restaurant restaurant);

    Restaurant updateRestaurant(Restaurant restaurant);

    void deleteRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();

    Restaurant findById(String googleID);

}
