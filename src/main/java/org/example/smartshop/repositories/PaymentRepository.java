package org.example.smartshop.repositories;

import org.example.smartshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderIdOrderByNumeroPaiementAsc(Long orderId);

    @Query("SELECT COALESCE(MAX(p.numeroPaiement), 0) FROM Payment p WHERE p.order.id = :orderId")
    Integer findMaxNumeroPaiementByOrderId(Long orderId);
}
