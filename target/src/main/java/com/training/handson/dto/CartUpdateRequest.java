package com.training.handson.dto;

public class CartUpdateRequest {
    private String sku;
    private Long quantity;

    public String getSku() {
        return sku;
    }

    public Long getQuantity() {
        return quantity;
    }

}
