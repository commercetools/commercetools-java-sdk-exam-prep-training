package com.training.handson.dto;

public class OrderRequest {

    private String cartId;
    private Long cartVersion;

    public String getCartId() { return cartId; }
    public Long getCartVersion() {
        return cartVersion;
    }

}
