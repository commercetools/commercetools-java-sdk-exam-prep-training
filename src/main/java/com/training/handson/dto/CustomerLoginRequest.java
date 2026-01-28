package com.training.handson.dto;

public class CustomerLoginRequest {

    private String storeKey;
    private String email;
    private String password;
    private String anonymousCartId;

    public String getStoreKey() { return storeKey; }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getAnonymousCartId() {
        return anonymousCartId;
    }

}
