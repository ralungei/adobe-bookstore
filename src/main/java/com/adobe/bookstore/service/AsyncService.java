package com.adobe.bookstore.service;

import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.model.OrderStatus;
import com.adobe.bookstore.model.Product;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncService {
    private ProductService productService;
    private OrderService orderService;

    public AsyncService(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @Async
    public void updateStock(Order order, List<Product> products) {
        this.productService.update(products);
        order.setStatus(OrderStatus.DONE.name());
        this.orderService.update(order);
    }
}
