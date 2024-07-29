package hiber.dao;

import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserDaoImp implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoImp.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }

    @Override
    public User getUserByCar(String model, int series) {
        String hql = "select user from User user where user.car.model =:model and user.car.series =:series";

        try (Session session = sessionFactory.openSession()) {
            TypedQuery<User> typedQuery = session.createQuery(hql);
            typedQuery.setParameter("model", model).setParameter("series", series);
            return typedQuery.getSingleResult();
        } catch (NoResultException ex) {
            LOGGER.log(Level.SEVERE, "Пользователь с таким автомобилем не найден");
            return null;
        }
    }
}
