package cz.fi.muni.pv243.store;

import cz.fi.muni.pv243.entity.food.RestaurantWeekData;

import java.util.List;

public interface RestaurantWeekDataStore {

    RestaurantWeekData addWeekData(RestaurantWeekData data);

    RestaurantWeekData updateWeekData(RestaurantWeekData data);

    void deleteWeekData(RestaurantWeekData data);

    RestaurantWeekData getThisWeekData(String googlePlaceID, int weekNumber);

    List<RestaurantWeekData> getAllWeekData();
}
