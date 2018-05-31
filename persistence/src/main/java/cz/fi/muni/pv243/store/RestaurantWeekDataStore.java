package cz.fi.muni.pv243.store;

import cz.fi.muni.pv243.entity.food.RestaurantWeekData;

import java.util.List;

public interface RestaurantWeekDataStore {

    List<RestaurantWeekData> getAllWeekData();

    RestaurantWeekData addWeekData(RestaurantWeekData data);

    RestaurantWeekData getThisWeekData(String googlePlaceID, int weekNumber);
}
