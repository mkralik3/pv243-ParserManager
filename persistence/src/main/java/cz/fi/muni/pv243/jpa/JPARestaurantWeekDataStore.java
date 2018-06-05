package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.entity.food.RestaurantWeekData;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.RestaurantWeekDataStore;

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
public class JPARestaurantWeekDataStore implements RestaurantWeekDataStore {

    private static final Logger LOGGER = Logger.getLogger( JPARestaurantWeekDataStore.class.getName() );

    @PersistenceContext(unitName = "ParserManager")
    private EntityManager em;

    @Override
    @Transactional
    public RestaurantWeekData addWeekData(RestaurantWeekData data){
        em.persist(data);
        em.flush();
        return data;
    }

    @Override
    @Transactional
    public RestaurantWeekData updateWeekData(RestaurantWeekData data) {
        em.merge(data);
        em.flush();
        return data;
    }

    @Override
    @Transactional
    public void deleteWeekData(RestaurantWeekData data) {
        em.remove(data);
    }

    @Override
    public List<RestaurantWeekData> getAllWeekData() {
        Query query = em.createQuery("SELECT r FROM RestaurantWeekData r");
        return (List<RestaurantWeekData>) query.getResultList();
    }

    @Override
    public RestaurantWeekData getThisWeekData(String googlePlaceID, int weekNumber) {
        try {
            return (RestaurantWeekData) em.createNamedQuery("findWeekDataByRestaurandIDAndWeekNumber")
                    .setParameter("googleID", googlePlaceID)
                    .setParameter("weekNumber", weekNumber)
                    .getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.info("There was not result for restaurant with google id: " + googlePlaceID + " and week number: " + weekNumber);
            return null;
        }
    }
}
