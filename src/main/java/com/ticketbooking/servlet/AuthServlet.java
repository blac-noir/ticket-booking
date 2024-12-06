package com.ticketbooking.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.ticketbooking.model.User;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends BaseServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getReader());
            
            if ("/login".equals(pathInfo)) {
                handleLogin(jsonNode, response);
            } else if ("/register".equals(pathInfo)) {
                handleRegister(jsonNode, response);
            } else {
                sendErrorResponse(response, "Invalid endpoint", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void handleLogin(JsonNode json, HttpServletResponse response) throws IOException {
        String email = json.get("email").asText();
        String password = json.get("password").asText();
        
        try {
            User user = facade.authenticateUser(email, password);
            String token = generateToken(user);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("user", user);
            
            sendJsonResponse(response, responseData);
        } catch (Exception e) {
            sendErrorResponse(response, "Invalid credentials", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    
    private void handleRegister(JsonNode json, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setEmail(json.get("email").asText());
        user.setPassword(json.get("password").asText());
        user.setFullName(json.get("fullName").asText());
        
        try {
            User registeredUser = facade.registerUser(user);
            String token = generateToken(registeredUser);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("user", registeredUser);
            responseData.put("success", true);
            
            sendJsonResponse(response, responseData);
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private String generateToken(User user) {
        // Simple token generation - in production, use a proper JWT library
        return "token_" + user.getId() + "_" + System.currentTimeMillis();
    }
} 