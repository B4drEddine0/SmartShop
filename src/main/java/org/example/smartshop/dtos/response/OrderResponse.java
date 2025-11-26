package org.example.smartshop.dtos.response;

import lombok.*;
import org.example.smartshop.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private Long clientId;
    private String clientNom;
    private LocalDateTime dateCreation;
    private Double sousTotal;
    private Double montantRemise;
    private Double montantHtApresRemise;
    private Double tva;
    private Double totalTtc;
    private Double montantRestant;
    private String codePromo;
    private OrderStatus status;
    private List<OrderItemResponse> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productNom;
        private Integer quantite;
        private Double prixUnitaire;
        private Double totalLigne;
    }
}
