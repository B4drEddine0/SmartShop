package org.example.smartshop.services;

import org.example.smartshop.dtos.request.OrderRequest;
import org.example.smartshop.dtos.response.OrderResponse;
import org.example.smartshop.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrders();
    Order getOrderEntityById(Long id);
    OrderResponse confirmOrder(Long id);
    OrderResponse cancelOrder(Long id);
}
