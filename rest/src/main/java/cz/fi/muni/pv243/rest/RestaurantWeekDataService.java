package cz.fi.muni.pv243.rest;

import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.entity.food.RestaurantWeekData;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.RestaurantStore;
import cz.fi.muni.pv243.store.RestaurantWeekDataStore;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Calendar;

@Path("/weekData")
public class RestaurantWeekDataService {

    @Inject
    @JPAStore
    private RestaurantWeekDataStore weekDataStore;

    @Inject
    @CachedStore
    private RestaurantStore restaurantStore;

    @GET
    @Path("{googleID}")
    @Produces("application/json")
    public Response getThisWeekMenu(@PathParam("googleID") String googleId) {
        Calendar cal = Calendar.getInstance();

        RestaurantWeekData data = weekDataStore.getThisWeekData(googleId, cal.get(Calendar.WEEK_OF_YEAR));

        if (data == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(data).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public RestaurantWeekData createWeek(RestaurantWeekData weekData) {
        String googleID = weekData.getRestaurant().getGooglePlaceID();
        Restaurant restaurantAlreadyExists = restaurantStore.findById(googleID);

        if (restaurantAlreadyExists != null) {
            weekData.setRestaurant(restaurantAlreadyExists);
            restaurantAlreadyExists.addMenuForWeek(weekData);
            restaurantStore.updateRestaurant(restaurantAlreadyExists);
        } else {
            weekDataStore.addWeekData(weekData);
        }

        return weekData;
    }
}
