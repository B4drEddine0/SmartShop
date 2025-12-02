package org.example.smartshop.services;

import org.example.smartshop.dtos.request.OrderRequest;
import org.example.smartshop.dtos.response.OrderResponse;
import org.example.smartshop.entity.*;
import org.example.smartshop.enums.CustomerTier;
import org.example.smartshop.enums.OrderStatus;
import org.example.smartshop.exception.BusinessException;
import org.example.smartshop.exception.ResourceNotFoundException;
import org.example.smartshop.mapper.OrderMapper;
import org.example.smartshop.repositories.OrderRepository;
import org.example.smartshop.services.ClientService;
import org.example.smartshop.services.ProductService;
import org.example.smartshop.services.PromoCodeService;
import org.example.smartshop.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private ProductService productService;

    @Mock
    private PromoCodeService promoCodeService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client client;
    private Product product;
    private Order order;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .nom("Test Client")
                .email("test@test.com")
                .tier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(0.0)
                .build();

        product = Product.builder()
                .id(1L)
                .nom("Laptop")
                .prixUnitaire(1000.0)
                .stock(10)
                .deleted(false)
                .orderItems(new ArrayList<>())
                .build();

        order = Order.builder()
                .id(1L)
                .client(client)
                .dateCreation(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .sousTotal(1000.0)
                .montantRemise(0.0)
                .montantHtApresRemise(1000.0)
                .tva(200.0)
                .totalTtc(1200.0)
                .montantRestant(1200.0)
                .tauxTva(0.20)
                .orderItems(new ArrayList<>())
                .build();

        OrderRequest.OrderItemRequest itemRequest = OrderRequest.OrderItemRequest.builder()
                .productId(1L)
                .quantite(1)
                .build();

        orderRequest = OrderRequest.builder()
                .clientId(1L)
                .items(List.of(itemRequest))
                .build();
    }

    @Test
    void createOrder_WithBasicTier_NoDiscount_Success() {
        when(clientService.getClientEntityById(1L)).thenReturn(client);
        when(productService.getProductEntityById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        OrderResponse response = orderService.createOrder(orderRequest);

        assertNotNull(response);
        verify(productService).decrementStock(1L, 1);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_WithSilverTier_AppliesDiscount() {
        client.setTier(CustomerTier.SILVER);
        product.setPrixUnitaire(600.0);

        when(clientService.getClientEntityById(1L)).thenReturn(client);
        when(productService.getProductEntityById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            assertEquals(600.0, savedOrder.getSousTotal());
            assertEquals(30.0, savedOrder.getMontantRemise()); // 5% of 600
            assertEquals(570.0, savedOrder.getMontantHtApresRemise());
            return savedOrder;
        });
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        orderService.createOrder(orderRequest);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_WithPromoCode_AppliesPromoDiscount() {
        orderRequest.setCodePromo("PROMO-2024");

        when(clientService.getClientEntityById(1L)).thenReturn(client);
        when(productService.getProductEntityById(1L)).thenReturn(product);
        when(promoCodeService.isValidPromoCode("PROMO-2024")).thenReturn(true);
        when(promoCodeService.getPromoDiscount()).thenReturn(0.05);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            assertEquals(50.0, savedOrder.getMontantRemise()); // 5% of 1000
            return savedOrder;
        });
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        orderService.createOrder(orderRequest);

        verify(promoCodeService, times(2)).isValidPromoCode("PROMO-2024"); // Changed to times(2)
    }


    @Test
    void createOrder_InsufficientStock_ReturnsRejectedOrder() {
        product.setStock(0);

        when(clientService.getClientEntityById(1L)).thenReturn(client);
        when(productService.getProductEntityById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            assertEquals(OrderStatus.REJECTED, savedOrder.getStatus());
            return savedOrder;
        });
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        orderService.createOrder(orderRequest);

        verify(productService, never()).decrementStock(anyLong(), anyInt());
    }

    @Test
    void createOrder_EmptyItems_ThrowsBusinessException() {
        orderRequest.setItems(new ArrayList<>());

        assertThrows(BusinessException.class, () -> orderService.createOrder(orderRequest));
    }

    @Test
    void confirmOrder_FullyPaid_Success() {
        order.setMontantRestant(0.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        OrderResponse response = orderService.confirmOrder(1L);

        assertNotNull(response);
        verify(clientService).updateClientStats(1L, 1200.0);
    }

    @Test
    void confirmOrder_NotFullyPaid_ThrowsBusinessException() {
        order.setMontantRestant(100.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class, () -> orderService.confirmOrder(1L));
    }

    @Test
    void confirmOrder_NotPending_ThrowsBusinessException() {
        order.setStatus(OrderStatus.CONFIRMED);
        order.setMontantRestant(0.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class, () -> orderService.confirmOrder(1L));
    }

    @Test
    void cancelOrder_PendingOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        OrderResponse response = orderService.cancelOrder(1L);

        assertNotNull(response);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void cancelOrder_NotPending_ThrowsBusinessException() {
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void getOrderById_OrderExists_ReturnsOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(new OrderResponse());

        OrderResponse response = orderService.getOrderById(1L);

        assertNotNull(response);
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_OrderNotFound_ThrowsResourceNotFoundException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void calculateDiscount_GoldTierWithMinimumAmount_AppliesDiscount() {
        client.setTier(CustomerTier.GOLD);
        product.setPrixUnitaire(900.0);

        when(clientService.getClientEntityById(1L)).thenReturn(client);
        when(productService.getProductEntityById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            assertEquals(90.0, savedOrder.getMontantRemise()); // 10% of 900
            return savedOrder;
        });
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        orderService.createOrder(orderRequest);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void calculateDiscount_PlatinumTierWithMinimumAmount_AppliesDiscount() {
        client.setTier(CustomerTier.PLATINUM);
        product.setPrixUnitaire(1500.0);

        when(clientService.getClientEntityById(1L)).thenReturn(client);
        when(productService.getProductEntityById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            assertEquals(225.0, savedOrder.getMontantRemise()); // 15% of 1500
            return savedOrder;
        });
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        orderService.createOrder(orderRequest);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void calculateTVA_OnAmountAfterDiscount() {
        client.setTier(CustomerTier.SILVER);
        product.setPrixUnitaire(600.0);

        when(clientService.getClientEntityById(1L)).thenReturn(client);
        when(productService.getProductEntityById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            assertEquals(570.0, savedOrder.getMontantHtApresRemise()); // 600 - 30
            assertEquals(114.0, savedOrder.getTva()); // 20% of 570
            assertEquals(684.0, savedOrder.getTotalTtc()); // 570 + 114
            return savedOrder;
        });
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        orderService.createOrder(orderRequest);

        verify(orderRepository).save(any(Order.class));
    }
}
