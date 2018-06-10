package cz.fi.muni.pv243.infinispan;

import cz.fi.muni.pv243.Configuration;
import cz.fi.muni.pv243.TestFactory;
import cz.fi.muni.pv243.entity.Day;
import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.Restaurant;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.store.ParserStore;
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
public class CachedParserStoreTest {

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Inject
    @CachedStore
    private ParserStore parserStore;

    @Inject
    @DefaultCacheConfiguration
    private Cache<Long, Parser> parserCache;

    @PersistenceContext
    private EntityManager manager;

    private Parser firstParser;
    private Parser secondParser;
    private Restaurant restaurant;

    @Before
    public void init() {
        firstParser = TestFactory.createParser("/a/b/c", false, Day.MONDAY);
        Restaurant restaurant1 = TestFactory.createRestaurant("testName2", "idPlac2", null,null);
        firstParser.setRestaurant(restaurant1);
        restaurant1.addParser(secondParser);
        manager.persist(firstParser);

        secondParser = TestFactory.createParser("/x/y/z", true, Day.FRIDAY);
        restaurant = TestFactory.createRestaurant("testName", "idPlac", null,null);
        secondParser.setRestaurant(restaurant);
        restaurant.addParser(secondParser);
        manager.remove(secondParser);
    }

    @After
    public void cleanUp(){
        parserCache.clear();
    }
    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void findParserTest(){
        assertThat(parserStore.findParser(firstParser.getId()))
                .isEqualTo(firstParser);
        assertThat(parserCache.containsKey(firstParser.getId()))
                .isTrue();
        manager.remove(firstParser);
        assertThat(parserStore.findParser(firstParser.getId())).isEqualTo(firstParser);//still in cache
        assertThat(parserStore.findParser(20l)).isNull();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void createParserTest() {
        parserStore.addParser(secondParser);

        assertThat(parserCache.containsKey(secondParser.getId()))
                .isTrue(); //is in the cache
        assertThat(parserStore.findParser(secondParser.getId()))
                .isEqualTo(secondParser)
                .isNotEqualTo(firstParser);
        assertThat(manager.contains(secondParser)).isTrue();
        assertThat(manager.find(Parser.class, secondParser.getId()))
                .isEqualTo(secondParser); //is in the persistence
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
        assertThat(parserCache.containsKey(firstParser.getId()))
                .isTrue();

        updated.setXpath("/c/d/e");
        parserStore.updateParser(updated);

        assertThat(parserCache.containsKey(updated.getId()))
                .isTrue();
        assertThat(parserCache.get(updated.getId()))
                .isEqualTo(updated); //updated in the cache
        assertThat(manager.contains(updated))
                .isTrue();
        assertThat(manager.find(Parser.class, updated.getId()))
                .isEqualTo(updated); //updated in the persistence
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void deleteParserTest(){
        manager.persist(secondParser);
        assertThat(parserStore.findParser(secondParser.getId()))
                .isEqualTo(secondParser);

        parserStore.deleteParser(secondParser);

        assertThat(parserCache.containsKey(secondParser.getId()))
                .isFalse(); //parser was deleted from cache
        assertThat(parserStore.findParser(secondParser.getId()))
                .isNull();
        assertThat(parserStore.findParser(firstParser.getId()))
                .isEqualTo(firstParser); //first is still there
        assertThat(manager.contains(secondParser))
                .isFalse();
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
