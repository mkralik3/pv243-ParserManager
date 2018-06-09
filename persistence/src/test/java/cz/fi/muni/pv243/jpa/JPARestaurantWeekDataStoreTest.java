package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.Configuration;
import cz.fi.muni.pv243.TestFactory;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.entity.food.RestaurantDailyData;
import cz.fi.muni.pv243.entity.food.RestaurantWeekData;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.RestaurantWeekDataStore;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class JPARestaurantWeekDataStoreTest {

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Inject
    @JPAStore
    protected RestaurantWeekDataStore weekDataStore;

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

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void addWeekDataTest(){
        assertThat(manager.contains(secondWeekData))
                .isFalse();
        weekDataStore.addWeekData(secondWeekData);
        assertThat(manager.contains(secondWeekData))
                .isTrue();
        assertThat(manager.contains(firstWeekData))
                .isTrue();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void updateWeekDataTest(){
        RestaurantWeekData restaurantWeekData = manager.find(RestaurantWeekData.class, firstWeekData.getId());

        restaurantWeekData.setWeekNumber(ANOTHER_WEEK_NUMBER);
        weekDataStore.updateWeekData(restaurantWeekData);

        assertThat(manager.find(RestaurantWeekData.class, firstWeekData.getId()))
                .hasFieldOrPropertyWithValue("weekNumber", ANOTHER_WEEK_NUMBER);
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), ANOTHER_WEEK_NUMBER))
                .isNotNull()
                .isEqualTo(restaurantWeekData);

    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void deleteWeekDataTest(){
        manager.persist(secondWeekData);
        assertThat(manager.contains(secondWeekData))
                .isTrue();
        weekDataStore.deleteWeekData(secondWeekData);
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
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), ANOTHER_WEEK_NUMBER))
                .isEqualTo(secondWeekData)
                .isNotEqualTo(firstWeekData);
        assertThat(weekDataStore.getThisWeekData(restaurant.getGooglePlaceID(), 10))
                .isNull();
        assertThat(weekDataStore.getThisWeekData("nothing", WEEK_NUMBER))
                .isNull();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getAllWeekData(){
        assertThat(weekDataStore.getAllWeekData())
                .hasSize(1)
                .contains(firstWeekData);
        manager.persist(secondWeekData);
        assertThat(weekDataStore.getAllWeekData())
                .hasSize(2)
                .contains(firstWeekData, secondWeekData);
    }
}
