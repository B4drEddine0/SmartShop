package org.example.smartshop.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Nom is required")
    private String nom;

    private String description;

    @NotNull(message = "Prix unitaire is required")
    @Min(value = 0, message = "Prix unitaire must be positive")
    private Double prixUnitaire;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be positive")
    private Integer stock;
}
