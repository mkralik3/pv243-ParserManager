package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.entity.User;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.UserStore;

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
public class JPAUserStore implements UserStore {

    @PersistenceContext(unitName="ParserManager")
    private EntityManager em;

    @Override
    public List<User> getAllUsers() {
        Query query = em.createQuery("SELECT p FROM User p");
        return (List<User>) query.getResultList();
    }

    @Override
    @Transactional
    public User addUser(User parser){
        em.persist(parser);
        return parser;
    }
}
