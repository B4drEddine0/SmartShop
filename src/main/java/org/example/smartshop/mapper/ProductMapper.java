package org.example.smartshop.mapper;

import org.example.smartshop.dtos.request.ProductRequest;
import org.example.smartshop.dtos.response.ProductResponse;
import org.example.smartshop.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    void updateEntity(ProductRequest request, @MappingTarget Product product);
}


