package org.example.smartshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.OrderRequest;
import org.example.smartshop.dtos.response.OrderResponse;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.services.OrderService;
import org.example.smartshop.utils.SessionUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private SessionUser getCurrentUser(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("CURRENT_USER");
        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }
        return user;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request,
                                                     HttpSession session) {
        SessionUser user = getCurrentUser(session);
        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id, HttpSession session) {
        SessionUser user = getCurrentUser(session);
        OrderResponse order = orderService.getOrderById(id);

        if (user.getRole() == UserRole.CLIENT && !order.getClientId().equals(user.getClientId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(HttpSession session) {
        SessionUser user = getCurrentUser(session);
        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
