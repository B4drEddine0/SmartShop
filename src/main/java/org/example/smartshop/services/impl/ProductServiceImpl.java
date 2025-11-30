package org.example.smartshop.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.dtos.request.ProductRequest;
import org.example.smartshop.dtos.response.ProductResponse;
import org.example.smartshop.entity.Product;
import org.example.smartshop.exception.BusinessException;
import org.example.smartshop.exception.ResourceNotFoundException;
import org.example.smartshop.mapper.ProductMapper;
import org.example.smartshop.repositories.ProductRepository;
import org.example.smartshop.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        product.setDeleted(false);
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = getProductEntityById(id);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProductEntityById(id);
        productMapper.updateEntity(request, product);
        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductEntityById(id);

        if (!product.getOrderItems().isEmpty()) {
            product.setDeleted(true);
            productRepository.save(product);
        } else {
            productRepository.delete(product);
        }
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findByDeletedFalse(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public void decrementStock(Long productId, Integer quantity) {
        Product product = getProductEntityById(productId);

        if (product.getStock() < quantity) {
            throw new BusinessException("Insufficient stock for product: " + product.getNom());
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
