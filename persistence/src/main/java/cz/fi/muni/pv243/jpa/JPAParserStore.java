package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.entity.Day;
import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.ParserStore;

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
public class JPAParserStore implements ParserStore {

    private static final Logger LOGGER = Logger.getLogger( JPAParserStore.class.getName() );


    @PersistenceContext(unitName = "ParserManager")
    private EntityManager em;

    @Override
    @Transactional
    public Parser addParser(Parser parser){
        em.persist(parser);
        em.flush();
        return parser;
    }

    @Override
    @Transactional
    public Parser updateParser(Parser parser) {
        em.merge(parser);
        em.flush();
        return parser;
    }

    @Override
    @Transactional
    public void deleteParser(Parser parser) {
        em.remove(parser);
    }

    @Override
    public List<Parser> getAllParsers(boolean confirmed) {
        Query q;
        if (confirmed) {
            q = em.createNamedQuery("findConfirmedParsers");
        }
        else {
            q = em.createNamedQuery("findUnconfirmedParsers");
        }
        return (List<Parser>) q.getResultList();
    }

    @Override
    public Parser findParser(Long id) {
        return em.find(Parser.class, id);
    }

    @Override
    public Parser getConfirmedParser(String restaurantId, Day day) {
        try {
            return (Parser) em.createNamedQuery("findConfirmedParserForRestaurantAndDay")
                    .setParameter("restaurantId", restaurantId)
                    .setParameter("day", day)
                    .getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.info("Parser not found for restaurant: " + restaurantId + " and day " + day);
            return null;
        }
    }
}
