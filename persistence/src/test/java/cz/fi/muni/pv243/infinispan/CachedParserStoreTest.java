package cz.fi.muni.pv243.infinispan;

import cz.fi.muni.pv243.Configuration;
import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class CachedParserStoreTest {

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Inject
    @CachedStore
    private ParserStore cachedParserStore;

    @Inject
    @JPAStore
    private ParserStore jpaParserStore;

    @PersistenceContext
    private EntityManager manager;

    private Parser firstParser;
    private Parser secondParser;

    @Before
    public void init() {
        firstParser = new Parser();
        firstParser.setXpath("/A/B/C");
        manager.persist(firstParser);

        secondParser = new Parser();
        secondParser.setXpath("/x/y/z");
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testGet(){
        assertThat(cachedParserStore.findParser(firstParser.getId())).isEqualTo(firstParser);
        assertThat(cachedParserStore.findParser(20l)).isNull();
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testCreate() {
        cachedParserStore.addParser(secondParser);

        assertThat(manager.contains(secondParser)).isTrue();
        assertThat(cachedParserStore.findParser(secondParser.getId())).isEqualTo(secondParser);
        assertThat(cachedParserStore.findParser(secondParser.getId())).isNotEqualTo(firstParser);

        assertThat(jpaParserStore.findParser(secondParser.getId())).isEqualTo(secondParser);
        assertThat(jpaParserStore.findParser(secondParser.getId())).isNotEqualTo(firstParser);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testGetAll(){
        manager.persist(firstParser);
        manager.persist(secondParser);

        assertThat(cachedParserStore.getAllParsers(false))
                .hasSize(2)
                .contains(firstParser, secondParser);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testUpdate(){
        Parser updated = cachedParserStore.findParser(firstParser.getId());
        updated.setXpath("/c/d/e");
        cachedParserStore.updateParser(updated);

        assertThat(manager.contains(updated)).isTrue();
        assertThat(cachedParserStore.findParser(firstParser.getId())).isEqualTo(updated);

        assertThat(jpaParserStore.findParser(firstParser.getId())).isEqualTo(updated);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void testDelete(){
        manager.persist(secondParser);
        cachedParserStore.deleteParser(secondParser);

        assertThat(manager.contains(secondParser)).isFalse();
        assertThat(cachedParserStore.findParser(secondParser.getId())).isNull();
        assertThat(cachedParserStore.findParser(firstParser.getId())).isEqualTo(firstParser);

        assertThat(jpaParserStore.findParser(secondParser.getId())).isNull();
        assertThat(jpaParserStore.findParser(firstParser.getId())).isEqualTo(firstParser);
    }
}
