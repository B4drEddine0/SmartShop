package org.example.smartshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.smartshop.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private Double sousTotal;

    @Column(nullable = false)
    @Builder.Default
    private Double montantRemise = 0.0;

    @Column(nullable = false)
    private Double montantHtApresRemise;

    @Column(nullable = false)
    private Double tva;

    @Column(nullable = false)
    private Double totalTtc;

    @Column(nullable = false)
    @Builder.Default
    private Double montantRestant = 0.0;

    private String codePromo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private Double tauxTva = 0.20;
}
