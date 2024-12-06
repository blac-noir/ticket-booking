package com.ticketbooking.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.ticketbooking.model.Ticket;
import com.ticketbooking.model.User;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/users/*")
public class UserServlet extends BaseServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String pathInfo = request.getPathInfo();
        Long userId = getUserIdFromToken(request);
        
        if (userId == null) {
            sendErrorResponse(response, "Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        try {
            if ("/profile".equals(pathInfo)) {
                User user = facade.getUserById(userId);
                sendJsonResponse(response, user);
            } else if ("/bookings".equals(pathInfo)) {
                List<Ticket> tickets = facade.getUserTickets(userId);
                sendJsonResponse(response, tickets);
            } else {
                sendErrorResponse(response, "Invalid endpoint", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        Long userId = getUserIdFromToken(request);
        
        if (userId == null) {
            sendErrorResponse(response, "Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        try {
            JsonNode json = objectMapper.readTree(request.getReader());
            User user = facade.getUserById(userId);
            user.setFullName(json.get("fullName").asText());
            
            User updatedUser = facade.updateUser(user);
            sendJsonResponse(response, updatedUser);
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Simple token parsing - in production, use proper JWT validation
            String[] parts = token.split("_");
            if (parts.length > 1) {
                return Long.parseLong(parts[1]);
            }
        }
        return null;
    }
} 