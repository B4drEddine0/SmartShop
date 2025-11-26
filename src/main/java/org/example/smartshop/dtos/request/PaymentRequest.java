package org.example.smartshop.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.smartshop.enums.PaymentMethod;
import org.example.smartshop.enums.PaymentStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Montant is required")
    @Min(value = 0, message = "Montant must be positive")
    private Double montant;

    @NotNull(message = "Type paiement is required")
    private PaymentMethod typePaiement;

    private PaymentStatus status;

    private String reference;

    private String banque;

    private LocalDate dateEcheance;

    private LocalDate dateEncaissement;
}
