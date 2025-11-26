package org.example.smartshop.mapper;

import org.example.smartshop.dtos.request.PaymentRequest;
import org.example.smartshop.dtos.response.PaymentResponse;
import org.example.smartshop.entity.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "order.id", target = "orderId")
    PaymentResponse toResponse(Payment payment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "numeroPaiement", ignore = true)
    @Mapping(target = "datePaiement", ignore = true)
    Payment toEntity(PaymentRequest request);
}