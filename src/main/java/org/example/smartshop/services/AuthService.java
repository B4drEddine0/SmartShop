package org.example.smartshop.services;

import jakarta.servlet.http.HttpSession;
import org.example.smartshop.dtos.request.LoginRequest;
import org.example.smartshop.utils.SessionUser;

public interface AuthService {
    SessionUser login(LoginRequest request, HttpSession session);
    void logout(HttpSession session);
    SessionUser getCurrentUser(HttpSession session);
}
