package org.taskapp.service;

import org.taskapp.security.JwtUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthService {

    public String login(String username, String password) {
        // In real app, validate against database
        if (validateCredentials(username, password)) {
            return JwtUtil.generateToken(username);
        }
        return null;
    }

    public boolean validateToken(String token) {
        return JwtUtil.isTokenValid(token);
    }

    public String getUsernameFromToken(String token) {
        return JwtUtil.extractUsername(token);
    }

    private boolean validateCredentials(String username, String password) {
        // Simplified validation - in real app, check against database
        return username != null && !username.isEmpty() &&
                password != null && password.length() >= 6;
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}