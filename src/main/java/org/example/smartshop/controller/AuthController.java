package org.example.smartshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.LoginRequest;
import org.example.smartshop.services.AuthService;
import org.example.smartshop.utils.SessionUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SessionUser> login(@Valid @RequestBody LoginRequest request, HttpSession session){
        SessionUser user = authService.login(request, session);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
        authService.logout(session);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<SessionUser> getCurrentUser(HttpSession session){
        SessionUser user = authService.getCurrentUser(session);
        if(user == null){
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(user);
    }
}
