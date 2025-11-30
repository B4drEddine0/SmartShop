package org.example.smartshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.ClientRequest;
import org.example.smartshop.dtos.response.ClientResponse;
import org.example.smartshop.dtos.response.OrderResponse;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.exception.UnauthorizedException;
import org.example.smartshop.services.ClientService;
import org.example.smartshop.utils.SessionUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    private SessionUser getCurrentUser(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("CURRENT_USER");
        if (user == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        return user;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request, HttpSession session) {
        SessionUser user = getCurrentUser(session);
        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ClientResponse response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id, HttpSession session) {
        SessionUser user = getCurrentUser(session);

        if (user.getRole() == UserRole.CLIENT && !id.equals(user.getClientId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ClientResponse response = clientService.getClientById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id,
                                                       @Valid @RequestBody ClientRequest request,
                                                       HttpSession session) {
        SessionUser user = getCurrentUser(session);
        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ClientResponse response = clientService.updateClient(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id, HttpSession session) {
        SessionUser user = getCurrentUser(session);
        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients(HttpSession session) {
        SessionUser user = getCurrentUser(session);
        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ClientResponse> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderResponse>> getClientOrderHistory(@PathVariable Long id, HttpSession session) {
        SessionUser user = getCurrentUser(session);

        if (user.getRole() == UserRole.CLIENT && !id.equals(user.getClientId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<OrderResponse> orders = clientService.getClientOrderHistory(id);
        return ResponseEntity.ok(orders);
    }
}
