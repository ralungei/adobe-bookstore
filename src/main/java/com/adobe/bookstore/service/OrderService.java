package com.adobe.bookstore.service;

import com.adobe.bookstore.dto.OrderProductDto;
import com.adobe.bookstore.model.Order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface OrderService {
    @NotNull Iterable<Order> getAllOrders();

    Long processOrder(@NotNull(message = "Order products can't be null.") @Valid List<OrderProductDto> productsDtos);

    Order create(@NotNull(message = "Order can't be null.") @Valid Order order);

    void update(@NotNull(message = "Order can't be null.") @Valid Order order);
}
