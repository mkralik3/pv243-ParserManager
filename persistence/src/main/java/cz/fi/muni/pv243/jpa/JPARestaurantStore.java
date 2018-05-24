package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.RestaurantStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Named
@ApplicationScoped
@JPAStore
public class JPARestaurantStore implements RestaurantStore {

    private static final Logger LOGGER = Logger.getLogger( JPARestaurantStore.class.getName() );

    @PersistenceContext(unitName = "ParserManager")
    private EntityManager em;

    @Override
    public List<Restaurant> getAllRestaurants() {
        Query query = em.createQuery("SELECT r FROM Restaurant r");
        return (List<Restaurant>) query.getResultList();
    }

    @Override
    @Transactional
    public Restaurant addRestaurant(Restaurant restaurant){
        em.persist(restaurant);
        em.flush();
        return restaurant;
    }

    @Override
    public Restaurant findById(String googleID) {
        Query query = em.createQuery("SELECT r FROM Restaurant r WHERE googlePlaceID = :id");
        query.setParameter("id", googleID);

        try {
            return (Restaurant) query.getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.info("There was not result for restaurant with google id: " + googleID);
            return null;
        }
    }

    @Override
    @Transactional
    public Restaurant updateRestaurant(Restaurant restaurant) {
        em.merge(restaurant);

        return restaurant;
    }
}
