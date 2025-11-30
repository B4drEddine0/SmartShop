package org.example.smartshop.services;

import org.example.smartshop.dtos.request.PaymentRequest;
import org.example.smartshop.dtos.request.UpdatePaymentStatusRequest;
import org.example.smartshop.dtos.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request);
    List<PaymentResponse> getPaymentsByOrderId(Long orderId);
    PaymentResponse getPaymentById(Long id);
}
