package cz.fi.muni.pv243.infinispan;

import cz.fi.muni.pv243.Configuration;
import cz.fi.muni.pv243.TestFactory;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.entity.food.RestaurantDailyData;
import cz.fi.muni.pv243.entity.food.RestaurantWeekData;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.store.RestaurantWeekDataStore;
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
public class CachedRestaurantWeekDataStoreTest {

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Inject
    @CachedStore
    RestaurantWeekDataStore weekDataStore;

    @Inject
    @DefaultCacheConfiguration
    private Cache<Long, RestaurantWeekData> weekDataCache;

    @PersistenceContext
    private EntityManager manager;

    private RestaurantWeekData firstWeekData;
    private RestaurantWeekData secondWeekData;
    private Restaurant restaurant;
    private static final int WEEK_NUMBER = 3;
    private static final int ANOTHER_WEEK_NUMBER = 5;

    @Before
    public void init() {
        restaurant = TestFactory.createRestaurant("firstRestaurant",
                "firstID","descript1", null);
        firstWeekData = TestFactory.createWeekData(restaurant,true,WEEK_NUMBER,null);
        RestaurantDailyData dailyData = TestFactory.createDailyData(null, null);
        dailyData.addMenuToDay(TestFactory.createFoodEntity("menu1",100,null));
        dailyData.addSoupToDay(TestFactory.createFoodEntity("soup",20,null));
        firstWeekData.addMenuForDay(dailyData);
        manager.persist(firstWeekData);

        secondWeekData = TestFactory.createWeekData(restaurant, false, ANOTHER_WEEK_NUMBER, null);
        manager.remove(secondWeekData);
    }

    @After
    public void cleanUp(){
        weekDataCache.clear();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void addWeekDataTest(){
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), ANOTHER_WEEK_NUMBER))
                .isNull();
        weekDataStore.addWeekData(secondWeekData);

        assertThat(weekDataCache.containsKey(secondWeekData.getId()))
                .isTrue(); //is in the cache
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), ANOTHER_WEEK_NUMBER))
                .isEqualTo(secondWeekData)
                .isNotEqualTo(firstWeekData);
        assertThat(manager.contains(secondWeekData))
                .isTrue();
        assertThat(manager.find(RestaurantWeekData.class, secondWeekData.getId()))
                .isEqualTo(secondWeekData);  //is in the persistence
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void updateWeekDataTest(){
        manager.persist(secondWeekData);
        RestaurantWeekData updated = weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), ANOTHER_WEEK_NUMBER);
        assertThat(weekDataCache.containsKey(secondWeekData.getId()))
                .isTrue();

        updated.setWeekNumber(20);
        weekDataStore.updateWeekData(updated);

        assertThat(weekDataCache.containsKey(updated.getId()))
                .isTrue();
        assertThat(weekDataCache.get(updated.getId()))
                .isEqualTo(updated)
                .hasFieldOrPropertyWithValue("weekNumber", 20); //updated in the cache
        assertThat(manager.contains(updated))
                .isTrue();
        assertThat(manager.find(RestaurantWeekData.class, updated.getId()))
                .isEqualTo(updated);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void deleteWeekDataTest(){

        manager.persist(secondWeekData);
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), ANOTHER_WEEK_NUMBER))
                .isEqualTo(secondWeekData);

        weekDataStore.deleteWeekData(secondWeekData);

        assertThat(weekDataCache.containsKey(secondWeekData.getId()))
                .isFalse(); //restaurant was deleted from cache
        assertThat(weekDataCache.get(secondWeekData.getId()))
                .isNull();
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), WEEK_NUMBER))
                .isEqualTo(firstWeekData); //first is still there
        assertThat(manager.contains(secondWeekData))
                .isFalse();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getThisWeekDataTest(){
        manager.persist(secondWeekData);
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), WEEK_NUMBER))
                .isEqualTo(firstWeekData)
                .isNotEqualTo(secondWeekData);
        assertThat(weekDataCache.containsKey(firstWeekData.getId()))
                .isTrue();
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), ANOTHER_WEEK_NUMBER))
                .isEqualTo(secondWeekData)
                .isNotEqualTo(firstWeekData);
        assertThat(weekDataCache.containsKey(secondWeekData.getId()))
                .isTrue();
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), 10))
                .isNull();
        assertThat(weekDataStore.getThisWeekData("nothing", WEEK_NUMBER))
                .isNull();
            }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getAllWeekDataTest(){
        assertThat(weekDataStore.getAllWeekData())
                .hasSize(1)
                .contains(firstWeekData);
        manager.persist(secondWeekData);
        assertThat(weekDataStore.getAllWeekData())
                .hasSize(2)
                .contains(firstWeekData, secondWeekData);
    }
}