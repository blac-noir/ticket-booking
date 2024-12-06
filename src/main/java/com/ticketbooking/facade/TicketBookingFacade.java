package com.ticketbooking.facade;

import com.ticketbooking.model.Event;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketBookingFacade {
    // User operations
    User registerUser(User user);
    User authenticateUser(String email, String password);
    User getUserById(Long userId);
    User updateUser(User user);
    
    // Event operations
    Event createEvent(Event event);
    Event getEventById(Long eventId);
    List<Event> getAvailableEvents();
    List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Ticket operations
    Ticket bookTicket(Long userId, Long eventId, String seatNumber);
    boolean cancelTicket(Long ticketId, Long userId);
    List<Ticket> getUserTickets(Long userId);
    List<Ticket> getEventTickets(Long eventId);
    long getAvailableSeatsCount(Long eventId);
} 