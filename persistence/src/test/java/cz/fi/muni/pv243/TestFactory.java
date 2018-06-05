package cz.fi.muni.pv243;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.entity.food.FoodEntity;
import cz.fi.muni.pv243.entity.food.Ingredient;
import cz.fi.muni.pv243.entity.food.RestaurantDailyData;
import cz.fi.muni.pv243.entity.food.RestaurantWeekData;

import java.util.List;
import java.util.Set;

public class TestFactory {

    public static Parser createParser(String xpath){
        Parser result = new Parser();
        result.setXpath(xpath);
        return result;
    }

    public static Restaurant createRestaurant(String name, String googlePlaceId, String description, List<RestaurantWeekData> menuForWeeks){
        Restaurant result = new Restaurant();
        result.setName(name);
        result.setGooglePlaceID(googlePlaceId);
        result.setDescription(description);
        if(menuForWeeks!=null) {
            menuForWeeks.stream().forEach(it -> {
                    result.addMenuForWeek(it);
            });
        }
        return result;
    }

    public static RestaurantWeekData createWeekData(Restaurant restaurant, boolean soapIncluded, int weekNumber, Set<RestaurantDailyData> menuForDays){
        RestaurantWeekData result = new RestaurantWeekData();
        result.setRestaurant(restaurant);
        result.setSoupIncludedInPrice(soapIncluded);
        result.setWeekNumber(weekNumber);
        if(menuForDays!=null){
            menuForDays.stream().forEach(it -> {
                result.addMenuForDay(it);
            });
        }
        return result;
    }

    public static RestaurantDailyData createDailyData(Set<FoodEntity> menu, Set<FoodEntity> soup){
        RestaurantDailyData result = new RestaurantDailyData();
        if(menu!=null){
            menu.stream().forEach(it -> {
                result.addMenuToDay(it);
            });
        }
        if(soup!=null){
            soup.stream().forEach(it -> {
                result.addSoupToDay(it);
            });
        }
        return result;
    }

    public static FoodEntity createFoodEntity(String name, Integer price, Set<Ingredient> tags){
        FoodEntity result = new FoodEntity();
        result.setName(name);
        result.setPrice(price);
        result.setTags(tags);
        return result;
    }
}
