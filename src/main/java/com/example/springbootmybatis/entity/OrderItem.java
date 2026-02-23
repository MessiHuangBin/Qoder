package com.example.springbootmybatis.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItem {
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("price")
    private double price;
}