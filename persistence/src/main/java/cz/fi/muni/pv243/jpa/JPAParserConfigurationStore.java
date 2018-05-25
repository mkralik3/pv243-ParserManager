package cz.fi.muni.pv243.jpa;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.ParserConfiguration;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.ParserConfigurationStore;

/**
 * 
 * @author Michaela Bocanova
 *
 */
@Named
@ApplicationScoped
@JPAStore
public class JPAParserConfigurationStore implements ParserConfigurationStore {

	@PersistenceContext(unitName = "ParserConfigurationManager")
    private EntityManager em;

    @Override
    public List<ParserConfiguration> getAllParsers() {
        Query query = em.createQuery("SELECT p FROM ParserConfiguration p");
        return (List<ParserConfiguration>) query.getResultList();
    }

    @Override
    @Transactional
    public ParserConfiguration addParser(ParserConfiguration parser){
        em.persist(parser);
        return parser;
    }
    
    @Override
    public ParserConfiguration findParser(Long id) {
        return em.find(ParserConfiguration.class, id);
    }
    
    @Override
	public ParserConfiguration updateParser(ParserConfiguration parser) {
    	return em.merge(parser);
    }
}
