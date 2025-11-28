package org.example.smartshop.services;

import org.example.smartshop.dtos.request.ProductRequest;
import org.example.smartshop.dtos.response.ProductResponse;
import org.example.smartshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Product getProductEntityById(Long id);
    void decrementStock(Long productId, Integer quantity);
}
