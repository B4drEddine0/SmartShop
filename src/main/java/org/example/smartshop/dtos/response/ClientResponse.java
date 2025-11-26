package org.example.smartshop.dtos.response;

import lombok.*;
import org.example.smartshop.enums.CustomerTier;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse {

    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private CustomerTier tier;
    private Integer totalOrders;
    private Double totalSpent;
    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;
}
