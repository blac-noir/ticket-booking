package com.ticketbooking.facade.impl;

import com.ticketbooking.dao.EventDAO;
import com.ticketbooking.dao.TicketDAO;
import com.ticketbooking.dao.UserDAO;
import com.ticketbooking.exception.EventNotFoundException;
import com.ticketbooking.exception.TicketBookingException;
import com.ticketbooking.exception.TicketNotAvailableException;
import com.ticketbooking.facade.TicketBookingFacade;
import com.ticketbooking.model.Event;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.TicketStatus;
import com.ticketbooking.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class TicketBookingFacadeImpl implements TicketBookingFacade {
    private final UserDAO userDAO;
    private final EventDAO eventDAO;
    private final TicketDAO ticketDAO;

    public TicketBookingFacadeImpl(UserDAO userDAO, EventDAO eventDAO, TicketDAO ticketDAO) {
        this.userDAO = userDAO;
        this.eventDAO = eventDAO;
        this.ticketDAO = ticketDAO;
    }

    @Override
    public User registerUser(User user) {
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            throw new TicketBookingException("User with email " + user.getEmail() + " already exists");
        }
        return userDAO.save(user);
    }

    @Override
    public User authenticateUser(String email, String password) {
        return userDAO.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new TicketBookingException("Invalid email or password"));
    }

    @Override
    public Event createEvent(Event event) {
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new TicketBookingException("Event date cannot be in the past");
        }
        return eventDAO.save(event);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventDAO.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
    }

    @Override
    public List<Event> getAvailableEvents() {
        return eventDAO.findAvailableEvents();
    }

    @Override
    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventDAO.findByDateRange(startDate, endDate);
    }

    @Override
    public Ticket bookTicket(Long userId, Long eventId, String seatNumber) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new TicketBookingException("User not found"));
        
        Event event = eventDAO.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (!ticketDAO.isTicketAvailable(eventId, seatNumber)) {
            throw new TicketNotAvailableException("Ticket not available for the selected seat");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setSeatNumber(seatNumber);
        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setBookingDate(LocalDateTime.now());

        return ticketDAO.save(ticket);
    }

    @Override
    public boolean cancelTicket(Long ticketId, Long userId) {
        Ticket ticket = ticketDAO.findById(ticketId)
                .orElseThrow(() -> new TicketBookingException("Ticket not found"));

        if (!ticket.getUser().getId().equals(userId)) {
            throw new TicketBookingException("Unauthorized to cancel this ticket");
        }

        if (ticket.getStatus() != TicketStatus.BOOKED) {
            throw new TicketBookingException("Ticket is not in a valid state for cancellation");
        }

        ticket.setStatus(TicketStatus.CANCELLED);
        ticketDAO.update(ticket);
        return true;
    }

    @Override
    public List<Ticket> getUserTickets(Long userId) {
        return ticketDAO.findByUserId(userId);
    }

    @Override
    public List<Ticket> getEventTickets(Long eventId) {
        return ticketDAO.findByEventId(eventId);
    }

    @Override
    public long getAvailableSeatsCount(Long eventId) {
        return eventDAO.getAvailableSeatsCount(eventId);
    }

    @Override
    public User getUserById(Long userId) {
        return userDAO.findById(userId)
                .orElseThrow(() -> new TicketBookingException("User not found with id: " + userId));
    }

    @Override
    public User updateUser(User user) {
        // Verify user exists
        userDAO.findById(user.getId())
                .orElseThrow(() -> new TicketBookingException("User not found with id: " + user.getId()));
        
        // Check if email is being changed and if it's already taken
        userDAO.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(user.getId())) {
                        throw new TicketBookingException("Email already in use: " + user.getEmail());
                    }
                });
        
        return userDAO.update(user);
    }
} 