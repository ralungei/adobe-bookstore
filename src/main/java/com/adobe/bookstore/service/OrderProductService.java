package com.adobe.bookstore.service;

import com.adobe.bookstore.model.OrderProduct;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface OrderProductService {

    OrderProduct create(@NotNull(message = "Products for order can't be null.") @Valid OrderProduct orderProduct);
}
