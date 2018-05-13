package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.ParserStore;

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
public class JPAParserStore implements ParserStore {

    @PersistenceContext(unitName = "ParserManager")
    private EntityManager em;

    @Override
    public List<Parser> getAllParsers() {
        Query query = em.createQuery("SELECT p FROM Parser p");
        return (List<Parser>) query.getResultList();
    }

    @Override
    @Transactional
    public Parser addParser(Parser parser){
        em.persist(parser);
        return parser;
    }
}
