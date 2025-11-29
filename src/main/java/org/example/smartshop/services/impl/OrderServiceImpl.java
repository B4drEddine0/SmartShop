package org.example.smartshop.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.OrderRequest;
import org.example.smartshop.dtos.response.OrderResponse;
import org.example.smartshop.entity.Client;
import org.example.smartshop.entity.Order;
import org.example.smartshop.entity.OrderItem;
import org.example.smartshop.entity.Product;
import org.example.smartshop.enums.CustomerTier;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.mapper.OrderMapper;
import org.example.smartshop.repositories.OrderRepository;
import org.example.smartshop.services.ClientService;
import org.example.smartshop.services.OrderService;
import org.example.smartshop.services.ProductService;
import org.example.smartshop.services.PromoCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ProductService productService;
    private final PromoCodeService promoCodeService;
    private final OrderMapper orderMapper;

    private static final Double TVA_RATE = 0.20;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        Client client = clientService.getClientEntityById(request.getClientId());

        // Check stock availability for all products
        for (OrderRequest.OrderItemRequest item : request.getItems()) {
            Product product = productService.getProductEntityById(item.getProductId());
            if (product.getStock() < item.getQuantite()) {
                Order rejectedOrder = createRejectedOrder(client, request);
                return orderMapper.toResponse(rejectedOrder);
            }
        }

        // Create order
        Order order = Order.builder()
                .client(client)
                .dateCreation(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .tauxTva(TVA_RATE)
                .orderItems(new ArrayList<>())
                .build();

        // Create order items and calculate subtotal
        Double sousTotal = 0.0;
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.getProductEntityById(itemRequest.getProductId());

            Double totalLigne = round(product.getPrixUnitaire() * itemRequest.getQuantite());

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantite(itemRequest.getQuantite())
                    .prixUnitaire(product.getPrixUnitaire())
                    .totalLigne(totalLigne)
                    .build();

            order.getOrderItems().add(orderItem);
            sousTotal += totalLigne;
        }

        order.setSousTotal(round(sousTotal));

        // Calculate discount
        Double montantRemise = calculateDiscount(client.getTier(), sousTotal, request.getCodePromo());
        order.setMontantRemise(round(montantRemise));

        if (request.getCodePromo() != null && promoCodeService.isValidPromoCode(request.getCodePromo())) {
            order.setCodePromo(request.getCodePromo());
        }

        // Calculate amounts
        Double montantHtApresRemise = sousTotal - montantRemise;
        order.setMontantHtApresRemise(round(montantHtApresRemise));

        Double tva = montantHtApresRemise * TVA_RATE;
        order.setTva(round(tva));

        Double totalTtc = montantHtApresRemise + tva;
        order.setTotalTtc(round(totalTtc));
        order.setMontantRestant(round(totalTtc));

        // Decrement stock
        for (OrderRequest.OrderItemRequest item : request.getItems()) {
            productService.decrementStock(item.getProductId(), item.getQuantite());
        }

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    private Order createRejectedOrder(Client client, OrderRequest request) {
        Order order = Order.builder()
                .client(client)
                .dateCreation(LocalDateTime.now())
                .status(OrderStatus.REJECTED)
                .sousTotal(0.0)
                .montantRemise(0.0)
                .montantHtApresRemise(0.0)
                .tva(0.0)
                .totalTtc(0.0)
                .montantRestant(0.0)
                .tauxTva(TVA_RATE)
                .orderItems(new ArrayList<>())
                .build();

        return orderRepository.save(order);
    }

    private Double calculateDiscount(CustomerTier tier, Double sousTotal, String promoCode) {
        Double loyaltyDiscount = 0.0;

        // Calculate loyalty discount
        switch (tier) {
            case SILVER:
                if (sousTotal >= 500) {
                    loyaltyDiscount = sousTotal * 0.05;
                }
                break;
            case GOLD:
                if (sousTotal >= 800) {
                    loyaltyDiscount = sousTotal * 0.10;
                }
                break;
            case PLATINUM:
                if (sousTotal >= 1200) {
                    loyaltyDiscount = sousTotal * 0.15;
                }
                break;
            default:
                loyaltyDiscount = 0.0;
        }

        // Calculate promo discount
        Double promoDiscount = 0.0;
        if (promoCode != null && promoCodeService.isValidPromoCode(promoCode)) {
            promoDiscount = sousTotal * promoCodeService.getPromoDiscount();
        }

        // Return the higher discount (not cumulative)
        return Math.max(loyaltyDiscount, promoDiscount);
    }

    private Double round(Double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = getOrderEntityById(id);
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public Order getOrderEntityById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(Long id) {
        Order order = getOrderEntityById(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be confirmed");
        }

        if (order.getMontantRestant() > 0) {
            throw new RuntimeException("Order must be fully paid before confirmation");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        Order updated = orderRepository.save(order);

        // Update client stats and loyalty tier
        clientService.updateClientStats(order.getClient().getId(), order.getTotalTtc());

        return orderMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = getOrderEntityById(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be canceled");
        }

        order.setStatus(OrderStatus.CANCELED);
        Order updated = orderRepository.save(order);

        return orderMapper.toResponse(updated);
    }

}
