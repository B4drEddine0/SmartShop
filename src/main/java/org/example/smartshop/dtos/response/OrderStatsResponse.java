package org.example.smartshop.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatsResponse {

    private Integer totalOrders;
    private Double totalSpent;
    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;
}
