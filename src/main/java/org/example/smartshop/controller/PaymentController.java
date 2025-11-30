package org.example.smartshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.PaymentRequest;
import org.example.smartshop.dtos.request.UpdatePaymentStatusRequest;
import org.example.smartshop.dtos.response.PaymentResponse;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.services.PaymentService;
import org.example.smartshop.utils.SessionUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private SessionUser getCurrentUser(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("CURRENT_USER");
        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }
        return user;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request,
                                                         HttpSession session) {
        SessionUser user = getCurrentUser(session);
        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable Long id,
                                                               @Valid @RequestBody UpdatePaymentStatusRequest request,
                                                               HttpSession session) {
        SessionUser user = getCurrentUser(session);
        if (user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PaymentResponse response = paymentService.updatePaymentStatus(id, request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrderId(@PathVariable Long orderId,
                                                                      HttpSession session) {
        getCurrentUser(session);
        List<PaymentResponse> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id, HttpSession session) {
        getCurrentUser(session);
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
}
