package com.training.handson.controllers;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.common.Address;
import com.training.handson.dto.CartCreateRequest;
import com.training.handson.dto.CartUpdateRequest;
import com.training.handson.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/in-store/{storeKey}/carts/")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("{id}")
    public CompletableFuture<ResponseEntity<Cart>> getCart(
            @PathVariable String storeKey,
            @PathVariable String id) {
        return cartService.getCartById(storeKey, id).thenApply(ResponseConverter::convert);
    }

    @PostMapping()
    public CompletableFuture<ResponseEntity<Cart>> createCart(
            @PathVariable String storeKey,
            @RequestBody CartCreateRequest cartCreateRequest) {
        return cartService.createCart(storeKey, cartCreateRequest).thenApply(ResponseConverter::convert);
    }

    @PostMapping("{id}/lineitems")
    public CompletableFuture<ResponseEntity<Cart>> addLineItem(
            @PathVariable String storeKey,
            @PathVariable String id,
            @RequestBody CartUpdateRequest cartUpdateRequest) {
        return cartService.addLineItem(storeKey, id, cartUpdateRequest).thenApply(ResponseConverter::convert);
    }

    @PostMapping("{id}/shipping-address")
    public CompletableFuture<ResponseEntity<Cart>> setShippingAddress(
            @PathVariable String storeKey,
            @PathVariable String id,
            @RequestBody Address address) {

            return cartService.setShippingAddress(storeKey, id, address).thenApply(ResponseConverter::convert);
    }

    @PostMapping("{id}/shipping-method")
    public CompletableFuture<ResponseEntity<Cart>> setShippingMethod(
            @PathVariable String storeKey,
            @PathVariable String id,
            @RequestParam String shippingMethodId) {

        return cartService.setShippingMethod(storeKey, id, shippingMethodId).thenApply(ResponseConverter::convert);
    }

}
