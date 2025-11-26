package org.example.smartshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.smartshop.enums.PaymentMethod;
import org.example.smartshop.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer numeroPaiement;

    @Column(nullable = false)
    private Double montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod typePaiement;

    @Column(nullable = false)
    private LocalDateTime datePaiement;

    private LocalDate dateEncaissement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.EN_ATTENTE;

    private String reference;

    private String banque;

    private LocalDate dateEcheance;
}
