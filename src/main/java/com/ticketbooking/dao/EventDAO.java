package com.ticketbooking.dao;

import com.ticketbooking.model.Event;
import java.time.LocalDateTime;
import java.util.List;

public interface EventDAO extends GenericDAO<Event> {
    List<Event> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Event> findAvailableEvents();
    long getAvailableSeatsCount(Long eventId);
} 