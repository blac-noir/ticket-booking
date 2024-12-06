package com.ticketbooking.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.ticketbooking.model.Ticket;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/tickets/*")
public class TicketServlet extends BaseServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String pathInfo = request.getPathInfo();
        Long userId = getUserIdFromToken(request);
        
        if (userId == null) {
            sendErrorResponse(response, "Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        try {
            if ("/book".equals(pathInfo)) {
                handleBooking(request, response, userId);
            } else if (pathInfo != null && pathInfo.matches("/\\d+/cancel")) {
                handleCancellation(pathInfo, response, userId);
            } else {
                sendErrorResponse(response, "Invalid endpoint", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void handleBooking(HttpServletRequest request, HttpServletResponse response, Long userId) 
            throws IOException {
        JsonNode json = objectMapper.readTree(request.getReader());
        Long eventId = json.get("eventId").asLong();
        String seatNumber = json.get("seatNumber").asText();
        
        try {
            Ticket ticket = facade.bookTicket(userId, eventId, seatNumber);
            sendJsonResponse(response, ticket);
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void handleCancellation(String pathInfo, HttpServletResponse response, Long userId) 
            throws IOException {
        Long ticketId = Long.parseLong(pathInfo.split("/")[1]);
        
        try {
            boolean cancelled = facade.cancelTicket(ticketId, userId);
            if (cancelled) {
                sendJsonResponse(response, Map.of("success", true));
            } else {
                sendErrorResponse(response, "Failed to cancel ticket", HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String[] parts = token.split("_");
            if (parts.length > 1) {
                return Long.parseLong(parts[1]);
            }
        }
        return null;
    }
} 