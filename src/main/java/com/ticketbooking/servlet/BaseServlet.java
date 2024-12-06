package com.ticketbooking.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbooking.config.ServiceConfig;
import com.ticketbooking.facade.TicketBookingFacade;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet extends HttpServlet {
    protected final TicketBookingFacade facade;
    protected final ObjectMapper objectMapper;

    public BaseServlet() {
        this.facade = ServiceConfig.getInstance().getTicketBookingFacade();
        this.objectMapper = new ObjectMapper();
    }

    protected void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.print(objectMapper.writeValueAsString(data));
        }
    }

    protected void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        sendJsonResponse(response, new ErrorResponse(message));
    }

    private static class ErrorResponse {
        @SuppressWarnings("unused")
        public final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
} 