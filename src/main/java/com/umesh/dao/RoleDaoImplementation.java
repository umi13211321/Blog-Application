package com.umesh.dao;

import com.umesh.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class RoleDaoImplementation implements RoleDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Role findRoleByName(String role) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Role> theQuery = currentSession.createQuery("from Role where name=:roleName", Role.class);
        theQuery.setParameter("roleName", role);

        Role theRole = null;
        try {
            theRole = theQuery.getSingleResult();
        } catch (Exception e) {
            theRole = null;
        }
        return theRole;
    }
}
