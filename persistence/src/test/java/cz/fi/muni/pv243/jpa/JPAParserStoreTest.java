package cz.fi.muni.pv243.jpa;

import static org.assertj.core.api.Assertions.*;
import cz.fi.muni.pv243.Configuration;
import cz.fi.muni.pv243.TestFactory;
import cz.fi.muni.pv243.entity.Day;
import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.entity.food.RestaurantDailyData;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.ParserStore;
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


@RunWith(Arquillian.class)
public class JPAParserStoreTest {

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Inject
    @JPAStore
    private ParserStore parserStore;

    @PersistenceContext
    private EntityManager manager;

    private Parser firstParser;
    private Parser secondParser;
    private Restaurant restaurant;

    @Before
    public void init() {
        firstParser = TestFactory.createParser("/a/b/c", false, Day.MONDAY);
        manager.persist(firstParser);

        secondParser = TestFactory.createParser("/x/y/z", true, Day.FRIDAY);
        restaurant = TestFactory.createRestaurant("testName", "idPlac", null,null);
        secondParser.setRestaurant(restaurant);
        restaurant.addParser(secondParser);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void findParserTest(){
        assertThat(parserStore.findParser(firstParser.getId())).isEqualTo(firstParser);
        assertThat(parserStore.findParser(20l)).isNull();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void createParserTest() {
        parserStore.addParser(secondParser);

        assertThat(manager.contains(secondParser)).isTrue();
        assertThat(parserStore.findParser(secondParser.getId())).isEqualTo(secondParser);
        assertThat(parserStore.findParser(secondParser.getId())).isNotEqualTo(firstParser);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getAllParsersTest(){
        manager.persist(secondParser);

        assertThat(parserStore.getAllParsers(false))
                .hasSize(1)
                .contains(firstParser);
        assertThat(parserStore.getAllParsers(true))
                .hasSize(1)
                .contains(secondParser);

        secondParser.setConfirmed(false);
        manager.persist(secondParser);
        assertThat(parserStore.getAllParsers(false))
                .hasSize(2)
                .contains(firstParser, secondParser);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void updateParserTest(){
        Parser updated = parserStore.findParser(firstParser.getId());
        updated.setXpath("/c/d/e");
        parserStore.updateParser(updated);

        assertThat(manager.contains(updated)).isTrue();
        assertThat(parserStore.findParser(firstParser.getId())).isEqualTo(updated);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void deleteParserTest(){
        manager.persist(secondParser);
        parserStore.deleteParser(secondParser);

        assertThat(manager.contains(secondParser)).isFalse();
        assertThat(parserStore.findParser(secondParser.getId())).isNull();
        assertThat(parserStore.findParser(firstParser.getId())).isEqualTo(firstParser);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getConfirmedParserTest(){
        manager.persist(secondParser);
        manager.persist(restaurant);

        assertThat(parserStore.getConfirmedParser(restaurant.getGooglePlaceID(), Day.FRIDAY))
                .isEqualTo(secondParser);
        assertThat(parserStore.getConfirmedParser(restaurant.getGooglePlaceID(), Day.MONDAY))
                .isNull();
    }
}
