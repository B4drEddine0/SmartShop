package org.example.smartshop.services.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.LoginRequest;
import org.example.smartshop.entity.User;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.exception.UnauthorizedException;
import org.example.smartshop.repositories.ClientRepository;
import org.example.smartshop.repositories.UserRepository;
import org.example.smartshop.services.AuthService;
import org.example.smartshop.utils.SessionUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final String SESSION_USER_KEY = "CURRENT_USER";
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Override
    public SessionUser login(LoginRequest request,HttpSession session){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new UnauthorizedException("Invalid username or password"));

        if (!user.getPassword().equals(request.getPassword())){
            throw new UnauthorizedException("Invalid username or password");
        }

        SessionUser sessionUser = SessionUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();

        if (user.getRole() == UserRole.CLIENT){
            clientRepository.findByUserId(user.getId())
                    .ifPresent(client -> sessionUser.setClientId(client.getId()));
        }

        session.setAttribute(SESSION_USER_KEY, sessionUser);
        return sessionUser;
    }
    @Override
    public void logout(HttpSession session){
        session.invalidate();
    }
    @Override
    public SessionUser getCurrentUser(HttpSession session){
        return (SessionUser) session.getAttribute(SESSION_USER_KEY);
    }
}













