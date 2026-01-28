package com.training.handson.controllers;

import com.commercetools.api.models.shipping_method.ShippingMethod;
import com.commercetools.api.models.shipping_method.ShippingMethodPagedQueryResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.training.handson.services.ShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/shipping-methods/")
public class ShippingController {

    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping()
    public CompletableFuture<ResponseEntity<ShippingMethodPagedQueryResponse>> getShippingMethods() {
        return shippingService.getShippingMethods().thenApply(ResponseConverter::convert);
    }

    @GetMapping("key={key}")
    public CompletableFuture<ResponseEntity<ShippingMethod>> getShippingMethodByKey(
            @PathVariable String key) {
        return shippingService.getShippingMethodByKey(key).thenApply(ResponseConverter::convert);
    }

    @RequestMapping(value = "key={key}", method = RequestMethod.HEAD)
    public CompletableFuture<ResponseEntity<JsonNode>> checkShippingMethodExistence(@PathVariable String key) {

        return shippingService.checkShippingMethodExistence(key).thenApply(ResponseConverter::convert);
    }

    @GetMapping("matching-location")
    public CompletableFuture<ResponseEntity<ShippingMethodPagedQueryResponse>> getShippingMethods(
            @RequestParam String countryCode) {
        return shippingService.getShippingMethodsByCountry(countryCode)
                .thenApply(response -> {
                    ShippingMethodPagedQueryResponse body = response.getBody();
                    body.setLimit(20L);
                    return response.withBody(body);
                })
                .thenApply(ResponseConverter::convert);
    }
}
