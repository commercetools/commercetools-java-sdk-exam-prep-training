package com.training.handson.controllers;

import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.training.handson.dto.CustomFieldRequest;
import com.training.handson.dto.CustomerLoginRequest;
import com.training.handson.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/in-store/{storeKey}/customers/")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("key={key}/custom-fields")
    public CompletableFuture<ResponseEntity<Customer>> getCustomerByKey(
            @PathVariable String storeKey,
            @PathVariable String key,
            @RequestBody CustomFieldRequest customFieldRequest) {
        return customerService.updateCustomerCustomFields(storeKey, key, customFieldRequest).thenApply(ResponseConverter::convert);
    }

    @PostMapping("login")
    public CompletableFuture<ResponseEntity<CustomerSignInResult>> loginCustomer(
            @PathVariable String storeKey,
            @RequestBody CustomerLoginRequest customerLoginRequest) {

        return customerService.loginCustomer(storeKey, customerLoginRequest).thenApply(ResponseConverter::convert);
    }
}
