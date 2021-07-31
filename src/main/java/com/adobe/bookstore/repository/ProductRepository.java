package com.adobe.bookstore.repository;

import com.adobe.bookstore.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
}
