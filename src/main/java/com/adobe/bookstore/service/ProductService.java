package com.adobe.bookstore.service;

import com.adobe.bookstore.model.Product;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ProductService {
    Product getProduct(String id);

    Product save(Product product);

    void update(List<Product> product);

}
