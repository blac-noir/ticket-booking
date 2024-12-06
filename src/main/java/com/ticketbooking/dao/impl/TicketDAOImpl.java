package com.ticketbooking.dao.impl;

import com.ticketbooking.dao.TicketDAO;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.TicketStatus;
import com.ticketbooking.util.HibernateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;

public class TicketDAOImpl extends GenericDAOImpl<Ticket> implements TicketDAO {

    @Override
    public List<Ticket> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
            Root<Ticket> root = query.from(Ticket.class);

            query.where(cb.equal(root.get("user").get("id"), userId));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Ticket> findByEventId(Long eventId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
            Root<Ticket> root = query.from(Ticket.class);

            query.where(cb.equal(root.get("event").get("id"), eventId));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Ticket> findByStatus(TicketStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
            Root<Ticket> root = query.from(Ticket.class);

            query.where(cb.equal(root.get("status"), status));
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public boolean isTicketAvailable(Long eventId, String seatNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Ticket> root = query.from(Ticket.class);

            query.select(cb.count(root));
            query.where(
                cb.and(
                    cb.equal(root.get("event").get("id"), eventId),
                    cb.equal(root.get("seatNumber"), seatNumber),
                    cb.equal(root.get("status"), TicketStatus.AVAILABLE)
                )
            );

            return session.createQuery(query).getSingleResult() > 0;
        }
    }
} 