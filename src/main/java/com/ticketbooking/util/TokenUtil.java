package com.ticketbooking.util;

import com.ticketbooking.model.User;

public class TokenUtil {
    private TokenUtil() {}
    
    public static String generateToken(User user) {
        return "token_" + user.getId() + "_" + System.currentTimeMillis();
    }
    
    public static Long getUserIdFromToken(String token) {
        if (token != null) {
            String[] parts = token.split("_");
            if (parts.length > 1) {
                try {
                    return Long.parseLong(parts[1]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
} 