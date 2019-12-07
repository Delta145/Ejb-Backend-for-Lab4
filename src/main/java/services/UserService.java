package services;

import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "хибернате козел")
    private EntityManager em;

    public User findUserByUsername(String username) {
        return em.find(User.class, username);
    }

    public User findUserByAuthToken(String token) {
        Query query = em.createQuery(("select u from User u WHERE u.authToken = :token"));
        query.setParameter("token", token);
        return (User) query.getSingleResult();
    }

    public void saveOrUpdateUser(User user) {
        em.merge(user);
    }
}
