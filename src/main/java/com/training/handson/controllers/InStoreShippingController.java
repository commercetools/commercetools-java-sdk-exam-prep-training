package com.training.handson.controllers;

import com.commercetools.api.models.shipping_method.ShippingMethod;
import com.commercetools.api.models.shipping_method.ShippingMethodPagedQueryResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.training.handson.services.ShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/in-store/{storeKey}/shipping-methods/")
public class InStoreShippingController {

    private final ShippingService shippingService;

    public InStoreShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping("matching-cart")
    public CompletableFuture<ResponseEntity<ShippingMethodPagedQueryResponse>> getShippingMethodsMatchingInStoreCart(
            @PathVariable String storeKey,
            @RequestParam String cartId) {
        return shippingService.getShippingMethodsByInStoreCart(storeKey, cartId)
                .thenApply(response -> {
                    ShippingMethodPagedQueryResponse body = response.getBody();
                    body.setLimit(20L);
                    return response.withBody(body);
                })
                .thenApply(ResponseConverter::convert);
    }
}
