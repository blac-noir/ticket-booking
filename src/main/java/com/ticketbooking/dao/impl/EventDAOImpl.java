package com.ticketbooking.dao.impl;

import com.ticketbooking.dao.EventDAO;
import com.ticketbooking.model.Event;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.TicketStatus;
import com.ticketbooking.util.HibernateUtil;

import jakarta.persistence.criteria.*;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

public class EventDAOImpl extends GenericDAOImpl<Event> implements EventDAO {

    @Override
    public List<Event> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Event> query = cb.createQuery(Event.class);
            Root<Event> root = query.from(Event.class);

            query.where(
                cb.between(root.get("eventDate"), startDate, endDate)
            );

            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public List<Event> findAvailableEvents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Event> query = cb.createQuery(Event.class);
            Root<Event> root = query.from(Event.class);

            query.where(
                cb.greaterThan(root.get("eventDate"), LocalDateTime.now())
            );

            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public long getAvailableSeatsCount(Long eventId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Ticket> root = query.from(Ticket.class);

            query.select(cb.count(root));
            query.where(
                cb.and(
                    cb.equal(root.get("event").get("id"), eventId),
                    cb.equal(root.get("status"), TicketStatus.AVAILABLE)
                )
            );

            return session.createQuery(query).getSingleResult();
        }
    }
} 