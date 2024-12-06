package com.ticketbooking.config;

import com.ticketbooking.dao.EventDAO;
import com.ticketbooking.dao.TicketDAO;
import com.ticketbooking.dao.UserDAO;
import com.ticketbooking.dao.impl.EventDAOImpl;
import com.ticketbooking.dao.impl.TicketDAOImpl;
import com.ticketbooking.dao.impl.UserDAOImpl;
import com.ticketbooking.facade.TicketBookingFacade;
import com.ticketbooking.facade.impl.TicketBookingFacadeImpl;

public class ServiceConfig {
    private static ServiceConfig instance;
    private final TicketBookingFacade ticketBookingFacade;

    private ServiceConfig() {
        // Initialize DAOs
        UserDAO userDAO = new UserDAOImpl();
        EventDAO eventDAO = new EventDAOImpl();
        TicketDAO ticketDAO = new TicketDAOImpl();

        // Initialize Facade with DAOs
        ticketBookingFacade = new TicketBookingFacadeImpl(userDAO, eventDAO, ticketDAO);
    }

    public static ServiceConfig getInstance() {
        if (instance == null) {
            synchronized (ServiceConfig.class) {
                if (instance == null) {
                    instance = new ServiceConfig();
                }
            }
        }
        return instance;
    }

    public TicketBookingFacade getTicketBookingFacade() {
        return ticketBookingFacade;
    }
} 