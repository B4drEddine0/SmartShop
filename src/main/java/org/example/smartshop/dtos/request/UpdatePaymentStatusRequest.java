package org.example.smartshop.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.smartshop.enums.PaymentStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePaymentStatusRequest {

    @NotNull(message = "Status is required")
    private PaymentStatus status;

    private LocalDate dateEncaissement;
}
