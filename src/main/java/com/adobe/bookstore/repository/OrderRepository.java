package com.adobe.bookstore.repository;

import com.adobe.bookstore.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
