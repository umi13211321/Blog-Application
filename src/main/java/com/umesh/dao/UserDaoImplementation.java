package com.umesh.dao;

import com.umesh.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImplementation implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User findByUserName(String username) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<User> theQuery = currentSession.createQuery("from User where username=:userName", User.class);
        theQuery.setParameter("userName", username);
        User theUser = null;
        try {
            theUser = theQuery.getSingleResult();
        } catch (Exception e) {
            theUser = null;
        }

        return theUser;
    }

    @Override
    public void save(User theUser) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(theUser);
    }

    @Override
    public List<String> getAllAuthors() {
        Session session = sessionFactory.getCurrentSession();
        Query<String> query = session.createSQLQuery("SELECT name from users where id in (Select user_id from user_roles where role_id = 2);");
        List<String> authors = query.getResultList();
        return authors;
    }
}
