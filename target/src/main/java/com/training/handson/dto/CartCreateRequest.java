package com.training.handson.dto;

public class CartCreateRequest {

    private String storeKey;
    private String sku;
    private Long quantity;
    private String country;
    private String currency;

    public String getStoreKey() { return storeKey; }

    public String getCurrency() {
        return currency;
    }

    public String getCountry() {
        return country;
    }

    public String getSku() {
        return sku;
    }

    public Long getQuantity() {
        return quantity;
    }

}