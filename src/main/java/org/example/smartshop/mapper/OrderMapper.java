package org.example.smartshop.mapper;


import org.example.smartshop.dtos.response.OrderResponse;
import org.example.smartshop.entity.Order;
import org.example.smartshop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source="client.id", target = "clientId")
    @Mapping(source = "client.nom", target = "clientNom")
    @Mapping(source = "orderItems", target = "items")
    OrderResponse toResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.nom", target = "productNom")
    OrderResponse.OrderItemResponse toItemResponse(OrderItem orderItem);

}