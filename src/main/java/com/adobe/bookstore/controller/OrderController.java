package com.adobe.bookstore.controller;

import com.adobe.bookstore.dto.OrderProductDto;
import com.adobe.bookstore.helper.CSVHelper;
import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.service.OrderService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Object list(@RequestParam String type) {
        Iterable<Order> ordersIterable = this.orderService.getAllOrders();

        if (type.equals("json"))
            return ordersIterable;
        else if (type.equals("csv")) {
            List<Order> ordersList = new ArrayList<>();
            ordersIterable.forEach(ordersList::add);
            ByteArrayInputStream in = CSVHelper.ordersToCSV(ordersList);

            String filename = "orders.csv";
            InputStreamResource file = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(file);
        } else throw new IllegalArgumentException();
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody List<OrderProductDto> productsDtos) {
        Long orderId = this.orderService.processOrder(productsDtos);
        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/orders/{id}")
                .buildAndExpand(orderId)
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(orderId, headers, HttpStatus.CREATED);
    }

}