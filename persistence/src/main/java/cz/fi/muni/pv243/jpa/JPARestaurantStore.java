package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.RestaurantStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Named
@ApplicationScoped
@JPAStore
public class JPARestaurantStore implements RestaurantStore {

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
        return restaurant;
    }
}
