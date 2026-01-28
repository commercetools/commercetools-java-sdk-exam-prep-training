package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.order.Order;
import com.training.handson.dto.OrderRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<Order>> createOrder(
            final String storeKey,
            final OrderRequest orderRequest) {

        return apiRoot
                .inStore(storeKey)
                .orders()
                .post(
                        orderFromCartDraftBuilder -> orderFromCartDraftBuilder
                                .cart(cartResourceIdentifierBuilder -> cartResourceIdentifierBuilder.id(orderRequest.getCartId()))
                                .version(orderRequest.getCartVersion())
                                .orderNumber("CT" + System.nanoTime())
                )
                .execute();
    }

}
