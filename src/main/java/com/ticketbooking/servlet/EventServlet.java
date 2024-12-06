package com.ticketbooking.servlet;

import com.ticketbooking.model.Event;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/events/*")
public class EventServlet extends BaseServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all events
                List<Event> events = facade.getAvailableEvents();
                sendJsonResponse(response, events);
            } else {
                // Get specific event
                Long eventId = Long.parseLong(pathInfo.substring(1));
                Event event = facade.getEventById(eventId);
                if (event != null) {
                    sendJsonResponse(response, event);
                } else {
                    sendErrorResponse(response, "Event not found", HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
} 