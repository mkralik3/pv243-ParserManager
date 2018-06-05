package cz.fi.muni.pv243.jpa;

import cz.fi.muni.pv243.entity.User;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.UserStore;

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
public class JPAUserStore implements UserStore {

    private static final Logger LOGGER = Logger.getLogger( JPAUserStore.class.getName() );

    @PersistenceContext(unitName="ParserManager")
    private EntityManager em;

    @Override
    @Transactional
    public User addUser(User parser){
        em.persist(parser);
        em.flush();
        return parser;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        em.merge(user);
        em.flush();
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        em.remove(user);
    }

    @Override
    public List<User> getAllUsers() {
        Query query = em.createQuery("SELECT p FROM User p");
        return (List<User>) query.getResultList();
    }

    @Override
    public User findById(Long id) {
        Query query = em.createQuery("SELECT u FROM User u WHERE id = :id");
        query.setParameter("id", id);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.info("There was not result for parser with id: " + id);
            return null;
        }
    }
}
