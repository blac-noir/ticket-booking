package com.ticketbooking.dao.impl;

import com.ticketbooking.dao.UserDAO;
import com.ticketbooking.model.User;
import com.ticketbooking.util.HibernateUtil;

import org.hibernate.Session;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Optional;

public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO {
    
    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.where(cb.equal(root.get("email"), email));
            
            return session.createQuery(query)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        }
    }
} 