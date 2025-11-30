package org.example.smartshop.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.PaymentRequest;
import org.example.smartshop.dtos.request.UpdatePaymentStatusRequest;
import org.example.smartshop.dtos.response.PaymentResponse;
import org.example.smartshop.entity.Order;
import org.example.smartshop.entity.Payment;
import org.example.smartshop.enums.PaymentMethod;
import org.example.smartshop.enums.PaymentStatus;
import org.example.smartshop.mapper.PaymentMapper;
import org.example.smartshop.repositories.PaymentRepository;
import org.example.smartshop.services.OrderService;
import org.example.smartshop.services.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final PaymentMapper paymentMapper;

    private static final Double CASH_LIMIT = 20000.0;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = orderService.getOrderEntityById(request.getOrderId());

        if (order.getMontantRestant() <= 0) {
            throw new RuntimeException("Order is already fully paid");
        }

        if (request.getMontant() > order.getMontantRestant()) {
            throw new RuntimeException("Payment amount exceeds remaining amount");
        }

        // Validate cash limit
        if (request.getTypePaiement() == PaymentMethod.ESPECES && request.getMontant() > CASH_LIMIT) {
            throw new RuntimeException("Cash payment cannot exceed " + CASH_LIMIT + " DH");
        }

        // Validate required fields based on payment method
        validatePaymentMethod(request);

        Integer nextNumber = paymentRepository.findMaxNumeroPaiementByOrderId(request.getOrderId()) + 1;

        Payment payment = paymentMapper.toEntity(request);
        payment.setOrder(order);
        payment.setNumeroPaiement(nextNumber);
        payment.setDatePaiement(LocalDateTime.now());

        // Set status based on payment method
        if (request.getTypePaiement() == PaymentMethod.ESPECES) {
            payment.setStatus(PaymentStatus.ENCAISSE);
            payment.setDateEncaissement(payment.getDatePaiement().toLocalDate());
        } else {
            payment.setStatus(request.getStatus() != null ? request.getStatus() : PaymentStatus.EN_ATTENTE);
            if (request.getDateEncaissement() != null) {
                payment.setDateEncaissement(request.getDateEncaissement());
            }
        }

        Payment saved = paymentRepository.save(payment);

        // Update order remaining amount
        updateOrderRemainingAmount(order);

        return paymentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        payment.setStatus(request.getStatus());

        if (request.getDateEncaissement() != null) {
            payment.setDateEncaissement(request.getDateEncaissement());
        }

        Payment updated = paymentRepository.save(payment);

        // Update order remaining amount
        updateOrderRemainingAmount(payment.getOrder());

        return paymentMapper.toResponse(updated);
    }


    @Override
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderIdOrderByNumeroPaiementAsc(orderId).stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return paymentMapper.toResponse(payment);
    }

    private void validatePaymentMethod(PaymentRequest request) {
        if (request.getTypePaiement() == PaymentMethod.CHEQUE) {
            if (request.getReference() == null || request.getBanque() == null || request.getDateEcheance() == null) {
                throw new RuntimeException("CHEQUE requires reference, banque, and dateEcheance");
            }
        }

        if (request.getTypePaiement() == PaymentMethod.VIREMENT) {
            if (request.getReference() == null || request.getBanque() == null) {
                throw new RuntimeException("VIREMENT requires reference and banque");
            }
        }

        if (request.getTypePaiement() == PaymentMethod.ESPECES) {
            if (request.getReference() == null) {
                throw new RuntimeException("ESPECES requires reference (receipt number)");
            }
        }
    }

    private void updateOrderRemainingAmount(Order order) {
        List<Payment> payments = paymentRepository.findByOrderIdOrderByNumeroPaiementAsc(order.getId());

        Double totalPaid = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.ENCAISSE)
                .mapToDouble(Payment::getMontant)
                .sum();

        Double remaining = order.getTotalTtc() - totalPaid;
        order.setMontantRestant(round(Math.max(0, remaining)));

        orderService.getOrderEntityById(order.getId());
    }

    private Double round(Double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
