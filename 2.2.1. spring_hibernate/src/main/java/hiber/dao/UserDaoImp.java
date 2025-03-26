package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      if (user.getCar() != null) {
         sessionFactory.getCurrentSession().saveOrUpdate(user.getCar());
      }
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   @Transactional
   public void saveCar(Car car) {
      sessionFactory.getCurrentSession().saveOrUpdate(car);
   }
   @Override
   public User getUserByCarModelAndSeries(String model, int series) {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(
              "SELECT u FROM User u WHERE u.car.model = :model AND u.car.series = :series", User.class);
      query.setParameter("model", model);
      query.setParameter("series", series);
      List<User> users = query.getResultList();
      return users.isEmpty() ? null : users.get(0);
   }
}
