package com.ticketbooking.util;

import com.ticketbooking.exception.TicketBookingException;
import com.ticketbooking.model.Event;
import com.ticketbooking.model.User;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    private ValidationUtil() {}

    public static void validateUser(User user) {
        if (user == null) {
            throw new TicketBookingException("User cannot be null");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new TicketBookingException("Invalid email format");
        }
        if (!isValidPassword(user.getPassword())) {
            throw new TicketBookingException("Password must be at least 8 characters long and contain at least one digit, " +
                    "one lowercase letter, one uppercase letter, and one special character");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new TicketBookingException("Full name cannot be empty");
        }
    }

    public static void validateEvent(Event event) {
        if (event == null) {
            throw new TicketBookingException("Event cannot be null");
        }
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new TicketBookingException("Event name cannot be empty");
        }
        if (event.getEventDate() == null || event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new TicketBookingException("Event date must be in the future");
        }
        if (event.getTotalSeats() == null || event.getTotalSeats() <= 0) {
            throw new TicketBookingException("Total seats must be greater than 0");
        }
        if (event.getTicketPrice() == null || event.getTicketPrice() < 0) {
            throw new TicketBookingException("Ticket price cannot be negative");
        }
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
} 