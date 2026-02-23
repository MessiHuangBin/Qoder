package com.example.springbootmybatis.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;


@Data
public class Order {
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("orderDate")
    private String orderDate;
    @JsonProperty("items")
    private List<OrderItem> items;
    @JsonProperty("totalAmount")
    private double totalAmount;
}