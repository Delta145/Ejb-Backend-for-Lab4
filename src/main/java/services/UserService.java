package services;

import entities.User;
import lombok.SneakyThrows;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "хибернате козел")
    private EntityManager em;
    @Resource
    private UserTransaction tr;

    public User findUserByUsername(String username) {
        return null;
    }

    @SneakyThrows
    public void saveUser(User user) {
        tr.begin();
        em.persist(user);
        tr.commit();
    }
}
