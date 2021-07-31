package com.adobe.bookstore.service;

import com.adobe.bookstore.dto.OrderProductDto;
import com.adobe.bookstore.exception.ResourceNotFoundException;
import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.model.OrderProduct;
import com.adobe.bookstore.model.OrderStatus;
import com.adobe.bookstore.model.Product;
import com.adobe.bookstore.repository.OrderRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private OrderProductService orderProductService;
    private ProductService productService;
    private AsyncService asyncService;


    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, OrderProductService orderProductService, @Lazy AsyncService asyncService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderProductService = orderProductService;
        this.asyncService = asyncService;
    }

    @Override
    public Iterable<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public Long processOrder(List<OrderProductDto> productsDtos) {
        List<Product> products = new ArrayList<>();

        if (productsDtos.isEmpty())
            throw new ResourceNotFoundException("Order must include at least one product");

        for (OrderProductDto dto : productsDtos) {
            Product p = productService.getProduct(dto.getId());

            if (Objects.isNull(p))
                throw new ResourceNotFoundException("Some product not found");
            else if (p.getQuantity() < dto.getQuantity())
                throw new ResourceNotFoundException("Some product is out of stock");

            p.setQuantity(p.getQuantity() - dto.getQuantity());
            products.add(p);
        }

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING.name());
        order = this.create(order);


        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < productsDtos.size(); i++)
            orderProducts.add(orderProductService.create(new OrderProduct(order, products.get(i), productsDtos.get(i).getQuantity())));

        order.setOrderProducts(orderProducts);

        this.update(order);

        this.asyncService.updateStock(order, products);

        return order.getId();
    }


    @Override
    public Order create(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public void update(Order order) {
        this.orderRepository.save(order);
    }
}