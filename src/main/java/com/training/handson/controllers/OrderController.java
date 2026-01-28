package com.training.handson.controllers;

import com.commercetools.api.models.order.Order;
import com.training.handson.dto.CustomFieldRequest;
import com.training.handson.dto.OrderRequest;
import com.training.handson.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/in-store/{storeKey}/orders/")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public CompletableFuture<ResponseEntity<Order>> createOrder(
            @PathVariable String storeKey,
            @RequestBody OrderRequest orderRequest) {

        return orderService.createOrder(storeKey, orderRequest).thenApply(ResponseConverter::convert);
    }
}
