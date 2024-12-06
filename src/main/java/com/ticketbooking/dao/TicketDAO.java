package com.ticketbooking.dao;

import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.TicketStatus;
import java.util.List;

public interface TicketDAO extends GenericDAO<Ticket> {
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByEventId(Long eventId);
    List<Ticket> findByStatus(TicketStatus status);
    boolean isTicketAvailable(Long eventId, String seatNumber);
} 