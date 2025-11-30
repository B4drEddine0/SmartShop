package org.example.smartshop.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.ClientRequest;
import org.example.smartshop.dtos.response.ClientResponse;
import org.example.smartshop.dtos.response.OrderResponse;
import org.example.smartshop.entity.Client;
import org.example.smartshop.entity.User;
import org.example.smartshop.enums.CustomerTier;
import org.example.smartshop.enums.UserRole;
import org.example.smartshop.exception.BusinessException;
import org.example.smartshop.exception.ResourceNotFoundException;
import org.example.smartshop.mapper.ClientMapper;
import org.example.smartshop.mapper.OrderMapper;
import org.example.smartshop.repositories.ClientRepository;
import org.example.smartshop.repositories.OrderRepository;
import org.example.smartshop.repositories.UserRepository;
import org.example.smartshop.services.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ClientMapper clientMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public ClientResponse createClient(ClientRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(UserRole.CLIENT)
                .build();

        User savedUser = userRepository.save(user);

        Client client = clientMapper.toEntity(request);
        client.setUser(savedUser);
        client.setTier(CustomerTier.BASIC);
        client.setTotalOrders(0);
        client.setTotalSpent(0.0);

        Client saved = clientRepository.save(client);
        return clientMapper.toResponse(saved);
    }


    @Override
    public ClientResponse getClientById(Long id) {
        Client client = getClientEntityById(id);
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional
    public ClientResponse updateClient(Long id, ClientRequest request) {
        Client client = getClientEntityById(id);

        if (!client.getEmail().equals(request.getEmail()) &&
                clientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        clientMapper.updateEntity(request, client);
        Client updated = clientRepository.save(client);
        return clientMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        Client client = getClientEntityById(id);
        User user = client.getUser();
        clientRepository.delete(client);
        if (user != null) {
            userRepository.delete(user);
        }
    }


    @Override
    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getClientOrderHistory(Long clientId) {
        return orderRepository.findByClientIdOrderByDateCreationDesc(clientId).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateClientStats(Long clientId, Double orderAmount) {
        Client client = getClientEntityById(clientId);

        client.setTotalOrders(client.getTotalOrders() + 1);
        client.setTotalSpent(client.getTotalSpent() + orderAmount);

        if (client.getFirstOrderDate() == null) {
            client.setFirstOrderDate(LocalDateTime.now());
        }
        client.setLastOrderDate(LocalDateTime.now());

        clientRepository.save(client);
        recalculateLoyaltyTier(clientId);
    }

    @Override
    @Transactional
    public void recalculateLoyaltyTier(Long clientId) {
        Client client = getClientEntityById(clientId);

        Integer totalOrders = client.getTotalOrders();
        Double totalSpent = client.getTotalSpent();

        CustomerTier newTier;

        if (totalOrders >= 20 || totalSpent >= 15000) {
            newTier = CustomerTier.PLATINUM;
        } else if (totalOrders >= 10 || totalSpent >= 5000) {
            newTier = CustomerTier.GOLD;
        } else if (totalOrders >= 3 || totalSpent >= 1000) {
            newTier = CustomerTier.SILVER;
        } else {
            newTier = CustomerTier.BASIC;
        }

        client.setTier(newTier);
        clientRepository.save(client);
    }

    @Override
    public Client getClientEntityById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }
}
