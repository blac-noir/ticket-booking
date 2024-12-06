package com.ticketbooking.dao;

import com.ticketbooking.model.User;
import java.util.Optional;

public interface UserDAO extends GenericDAO<User> {
    Optional<User> findByEmail(String email);
} 