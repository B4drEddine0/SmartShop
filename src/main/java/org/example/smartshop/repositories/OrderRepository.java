package org.example.smartshop.repositories;

import org.example.smartshop.entity.Order;
import org.example.smartshop.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientIdOrderByDateCreationDesc(Long clientId);
    List<Order> findByClientIdAndStatus(Long clientId, OrderStatus status);

}