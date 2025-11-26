package org.example.smartshop.dtos.response;

import lombok.*;
import org.example.smartshop.enums.PaymentMethod;
import org.example.smartshop.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private Integer numeroPaiement;
    private Double montant;
    private PaymentMethod typePaiement;
    private LocalDateTime datePaiement;
    private LocalDate dateEncaissement;
    private PaymentStatus status;
    private String reference;
    private String banque;
    private LocalDate dateEcheance;
}
