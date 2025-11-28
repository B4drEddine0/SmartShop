package org.example.smartshop.services;

import org.example.smartshop.dtos.request.ClientRequest;
import org.example.smartshop.dtos.response.ClientResponse;
import org.example.smartshop.dtos.response.OrderResponse;
import org.example.smartshop.entity.Client;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(ClientRequest request);
    ClientResponse getClientById(Long id);
    ClientResponse updateClient(Long id, ClientRequest request);
    void deleteClient(Long id);
    List<ClientResponse> getAllClients();
    List<OrderResponse> getClientOrderHistory(Long clientId);
    void updateClientStats(Long clientId, Double orderAmount);
    void recalculateLoyaltyTier(Long clientId);
    Client getClientEntityById(Long id);
}
