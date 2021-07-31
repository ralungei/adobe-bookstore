package com.adobe.bookstore.repository;

import com.adobe.bookstore.model.OrderProduct;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, Long> {
}
