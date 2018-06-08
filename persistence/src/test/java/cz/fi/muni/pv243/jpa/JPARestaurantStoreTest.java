package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.Configuration;
import cz.fi.muni.pv243.TestFactory;
import cz.fi.muni.pv243.entity.Day;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.entity.food.RestaurantDailyData;
import cz.fi.muni.pv243.entity.food.RestaurantWeekData;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.RestaurantStore;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class JPARestaurantStoreTest {

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Inject
    @JPAStore
    protected RestaurantStore restaurantStore;

    @PersistenceContext
    private EntityManager manager;

    private Restaurant firstRestaurant;
    private Restaurant secondRestaurant;

    @Before
    public void init() {
        firstRestaurant = TestFactory.createRestaurant("firstRestaurant",
                "firstID","descript1", null);
        firstRestaurant.addParser(TestFactory.createParser("/a/b/c", true, Day.MONDAY));
        RestaurantWeekData weekData = TestFactory.createWeekData(firstRestaurant,true,3,null);
        RestaurantDailyData dailyData = TestFactory.createDailyData(null, null);
        dailyData.addMenuToDay(TestFactory.createFoodEntity("menu1",100,null));
        dailyData.addSoupToDay(TestFactory.createFoodEntity("soup",20,null));
        weekData.addMenuForDay(dailyData);
        firstRestaurant.addMenuForWeek(weekData);

        manager.persist(firstRestaurant);

        secondRestaurant = TestFactory.createRestaurant("res2", "id2", null, null);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void addRestaurantTest(){
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isNull();
        restaurantStore.addRestaurant(secondRestaurant);
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isEqualTo(secondRestaurant)
                .isNotEqualTo(firstRestaurant);
        assertThat(manager.contains(secondRestaurant)).isTrue();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void updateRestaurantTest(){
        manager.persist(secondRestaurant);
        Restaurant updatedRestaurant = restaurantStore.findById(secondRestaurant.getGooglePlaceID());
        updatedRestaurant.setName("newName");
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isEqualTo(updatedRestaurant)
                .isNotEqualTo(firstRestaurant);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void deleteRestaurantTest(){
        manager.persist(secondRestaurant);
        restaurantStore.deleteRestaurant(secondRestaurant);
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isNull();
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
        manager.persist(secondRestaurant);
        assertThat(restaurantStore.findById(firstRestaurant.getGooglePlaceID()))
                .isEqualTo(firstRestaurant)
                .isNotEqualTo(secondRestaurant);
        assertThat(restaurantStore.findById(secondRestaurant.getGooglePlaceID()))
                .isEqualTo(secondRestaurant)
                .isNotEqualTo(firstRestaurant);
    }
}
