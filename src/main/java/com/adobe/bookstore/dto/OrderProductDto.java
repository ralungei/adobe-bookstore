package com.adobe.bookstore.dto;

public class OrderProductDto {

    private String id;
    private Integer quantity;

    public OrderProductDto(String id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderProductDto{" +
                "id='" + id + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}