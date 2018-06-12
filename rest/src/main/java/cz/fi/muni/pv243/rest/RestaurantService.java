package cz.fi.muni.pv243.rest;

import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.store.RestaurantStore;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
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
    @RolesAllowed({"admin"})
    public Restaurant createRestaurant(@Valid Restaurant restaurant) {
        return restaurantStore.addRestaurant(restaurant);
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateRestaurant(@Valid Restaurant restaurant) {
        Restaurant toUpdate = restaurantStore.findById(restaurant.getGooglePlaceID());

        if (toUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        toUpdate.setName(restaurant.getName());
        toUpdate.setDescription(restaurant.getDescription());
        restaurantStore.updateRestaurant(toUpdate);

        return Response.ok().entity(toUpdate).build();
    }

    @GET
    @Path("{googleID}")
    @Produces("application/json")
    public Response getRestaurant(@PathParam("googleID") String googleId) {
        Restaurant data = restaurantStore.findById(googleId);

        if (data == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(data).build();
    }

    @GET
    @Path("{googleID}/parsers")
    @Produces("application/json")
    public Response getRestaurantsParsers(@PathParam("googleID") String googleId) {
        Restaurant data = restaurantStore.findById(googleId);

        if (data == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(data.getParsers()).build();
    }
}