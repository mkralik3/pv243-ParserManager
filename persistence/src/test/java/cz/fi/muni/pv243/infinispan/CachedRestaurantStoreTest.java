package cz.fi.muni.pv243.infinispan;

import cz.fi.muni.pv243.Configuration;
import cz.fi.muni.pv243.TestFactory;
import cz.fi.muni.pv243.entity.Day;
import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.entity.food.RestaurantDailyData;
import cz.fi.muni.pv243.entity.food.RestaurantWeekData;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.store.RestaurantStore;
import org.infinispan.Cache;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class CachedRestaurantStoreTest {

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Inject
    @CachedStore
    RestaurantStore restaurantStore;

    @Inject
    @DefaultCacheConfiguration
    private Cache<String, Restaurant> restaurantCache;

    @PersistenceContext
    private EntityManager manager;

    private Restaurant firstRestaurant;
    private Restaurant secondRestaurant;

    @Before
    public void init() {
        firstRestaurant = TestFactory.createRestaurant("firstRestaurant",
                "firstID","descript1", null);
        Parser parser = TestFactory.createParser("/a/b/c", true, Day.MONDAY);
        parser.setRestaurant(firstRestaurant);
        firstRestaurant.addParser(parser);
        RestaurantWeekData weekData = TestFactory.createWeekData(firstRestaurant,true,3,null);
        RestaurantDailyData dailyData = TestFactory.createDailyData(null, null);
        dailyData.addMenuToDay(TestFactory.createFoodEntity("menu1",100,null));
        dailyData.addSoupToDay(TestFactory.createFoodEntity("soup",20,null));
        weekData.addMenuForDay(dailyData);
        firstRestaurant.addMenuForWeek(weekData);

        manager.persist(firstRestaurant);
        manager.persist(parser);

        secondRestaurant = TestFactory.createRestaurant("res2", "id2", null, null);
        manager.remove(secondRestaurant);
    }

    @After
    public void cleanUp(){
        restaurantCache.clear();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void addRestaurantTest(){
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isNull();
        restaurantStore.addRestaurant(secondRestaurant);

        assertThat(restaurantCache.containsKey(secondRestaurant.getGooglePlaceID()))
                .isTrue(); //is in the cache
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isEqualTo(secondRestaurant)
                .isNotEqualTo(firstRestaurant);
        assertThat(manager.contains(secondRestaurant))
                .isTrue();
        assertThat(manager.find(Restaurant.class, secondRestaurant.getGooglePlaceID()))
                .isEqualTo(secondRestaurant);  //is in the persistence
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void updateRestaurantTest(){
        manager.persist(secondRestaurant);
        Restaurant updatedRestaurant = restaurantStore.findById(secondRestaurant.getGooglePlaceID());
        assertThat(restaurantCache.containsKey(secondRestaurant.getGooglePlaceID()))
                .isTrue();

        updatedRestaurant.setName("newName");
        restaurantStore.updateRestaurant(updatedRestaurant);

        assertThat(restaurantCache.containsKey(updatedRestaurant.getGooglePlaceID()))
                .isTrue();
        assertThat(restaurantCache.get(updatedRestaurant.getGooglePlaceID()))
                .isEqualTo(updatedRestaurant)
                .hasFieldOrPropertyWithValue("name", "newName"); //updated in the cache
        assertThat(manager.contains(updatedRestaurant))
                .isTrue();
        assertThat(manager.find(Restaurant.class, updatedRestaurant.getGooglePlaceID()))
                .isEqualTo(updatedRestaurant);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void deleteRestaurantTest(){
        manager.persist(secondRestaurant);
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isEqualTo(secondRestaurant);

        restaurantStore.deleteRestaurant(secondRestaurant);

        assertThat(restaurantCache.containsKey(secondRestaurant.getGooglePlaceID()))
                .isFalse(); //restaurant was deleted from cache
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isNull();
        assertThat(restaurantStore.findById(firstRestaurant.getGooglePlaceID()))
                .isEqualTo(firstRestaurant); //first is still there
        assertThat(manager.contains(secondRestaurant))
                .isFalse();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getAllRestaurantsTest(){
        assertThat(restaurantStore.getAllRestaurants())
                .hasSize(1)
                .contains(firstRestaurant);
        manager.persist(secondRestaurant);
        assertThat(restaurantStore.getAllRestaurants())
                .hasSize(2)
                .contains(firstRestaurant, secondRestaurant);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void findByIdTest(){
        assertThat(restaurantStore.findById(firstRestaurant.getGooglePlaceID()))
                .isEqualTo(firstRestaurant)
                .isNotEqualTo(secondRestaurant);
        assertThat(restaurantCache.containsKey(firstRestaurant.getGooglePlaceID()))
                .isTrue();
        manager.remove(firstRestaurant);
        assertThat(restaurantStore.findById(firstRestaurant.getGooglePlaceID()))
                .isEqualTo(firstRestaurant);//still in cache
        assertThat(restaurantStore.findById("nonexistID")).isNull();
    }
}
