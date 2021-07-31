package com.adobe.bookstore.service;

import com.adobe.bookstore.exception.ResourceNotFoundException;
import com.adobe.bookstore.model.Product;
import com.adobe.bookstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProduct(String id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public void update(List<Product> products) {
        for (Product p : products)
            this.productRepository.save(p);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }
}