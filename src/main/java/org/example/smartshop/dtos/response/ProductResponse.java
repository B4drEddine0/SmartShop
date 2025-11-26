package org.example.smartshop.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String nom;
    private String description;
    private Double prixUnitaire;
    private Integer stock;
}
