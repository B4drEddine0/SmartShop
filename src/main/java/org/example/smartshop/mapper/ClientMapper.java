package org.example.smartshop.mapper;

import org.example.smartshop.dtos.request.ClientRequest;
import org.example.smartshop.dtos.response.ClientResponse;
import org.example.smartshop.entity.Client;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientMapper toResponse(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tier", ignore = true)
    @Mapping(target = "totalOrders", ignore = true)
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "firstOrderDate", ignore = true)
    @Mapping(target = "lastOrderDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Client toEntity(ClientRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tier", ignore = true)
    @Mapping(target = "totalOrders", ignore = true)
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "firstOrderDate", ignore = true)
    @Mapping(target = "lastOrderDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateEntity(ClientRequest request, @MappingTarget Client client);

}
