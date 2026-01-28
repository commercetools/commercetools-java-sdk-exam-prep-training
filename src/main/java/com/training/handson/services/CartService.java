package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.common.Address;
import com.training.handson.dto.CartCreateRequest;
import com.training.handson.dto.CartUpdateRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CartService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<Cart>> getCartById(final String storeKey, final String cartId) {

            return apiRoot
                    .inStore(storeKey)
                    .carts()
                    .withId(cartId)
                    .get()
                    .execute();
    }

    public CompletableFuture<ApiHttpResponse<Cart>> createCart(
            final String storeKey,
            final CartCreateRequest cartCreateRequest
            ) {
                return apiRoot
                        .inStore(storeKey)
                        .carts()
                        .post(
                                cartDraftBuilder -> cartDraftBuilder
                                        .currency(cartCreateRequest.getCurrency())
                                        .deleteDaysAfterLastModification(90L)
                                        .anonymousId("an" + System.nanoTime())
                                        .country(cartCreateRequest.getCountry())
                        )
                        .execute();
    }

    public CompletableFuture<ApiHttpResponse<Cart>> addLineItem(
            final String storeKey,
            final String cartId,
            CartUpdateRequest cartUpdateRequest){
        return this.getCartById(storeKey, cartId)
                .thenApply(ApiHttpResponse::getBody)
                .thenCompose(cart -> {
                    CartUpdateAction cartUpdateAction =
                            CartAddLineItemActionBuilder.of()
                                    .sku(cartUpdateRequest.getSku())
                                    .quantity(cartUpdateRequest.getQuantity())
                                    .build();
                    return
                            apiRoot
                                    .inStore(storeKey)
                                    .carts()
                                    .withId(cart.getId())
                                    .post(
                                            cartUpdateBuilder -> cartUpdateBuilder
                                                    .version(10L)
                                                    .actions(cartUpdateAction)
                                    )
                                    .execute();
                });
    }

    public CompletableFuture<ApiHttpResponse<Cart>> setShippingAddress(
            final String storeKey,
            final String cartId,
            final Address address) {

        return this.getCartById(storeKey, cartId)
            .thenApply(ApiHttpResponse::getBody)
            .thenCompose(cart -> {
                return apiRoot
                    .inStore(storeKey)
                    .carts()
                    .withId(cartId)
                    .post(
                        cartUpdateBuilder -> cartUpdateBuilder
                            .version(cart.getVersion())
                            .plusActions(cartUpdateActionBuilder -> cartUpdateActionBuilder
                                    .setShippingAddressBuilder()
                                    .address(address))
                            .plusActions(cartUpdateActionBuilder -> cartUpdateActionBuilder
                                    .setCustomerEmailBuilder().email(address.getEmail()))
                    )
                    .execute();
            });

    }

    public CompletableFuture<ApiHttpResponse<Cart>> setShippingMethod(
            final String storeKey,
            final String cartId,
            final String shippingMethodId) {

        return this.getCartById(storeKey, cartId)
                .thenApply(ApiHttpResponse::getBody)
                .thenCompose(cart -> {
                    return apiRoot
                            .inStore(storeKey)
                            .carts()
                            .withId(cartId)
                            .post(
                                    cartUpdateBuilder -> cartUpdateBuilder
                                            .version(cart.getVersion())
                                            .plusActions(
                                                    cartUpdateActionBuilder -> cartUpdateActionBuilder
                                                            .setShippingMethodBuilder()
                                                            .shippingMethod(
                                                                    shippingMethodResourceIdentifierBuilder -> shippingMethodResourceIdentifierBuilder
                                                                            .id(shippingMethodId)
                                                            )
                                            )
                            )
                            .execute();
                });
    }

    public CompletableFuture<ApiHttpResponse<Cart>> recalculate(final String storeKey, final String cartId) {

        return this.getCartById(storeKey, cartId)
                .thenApply(ApiHttpResponse::getBody)
                .thenCompose(cart -> {
                    return
                            apiRoot
                                    .inStore(storeKey)
                                    .carts()
                                    .withId(cart.getId())
                                    .post(
                                            cartUpdateBuilder -> cartUpdateBuilder
                                                    .version(cart.getVersion())
                                                    .plusActions(
                                                            cartUpdateActionBuilder -> cartUpdateActionBuilder
                                                                    .recalculateBuilder()
                                                                    .updateProductData(true)
                                                    )
                                    )
                                    .execute();
                });

    }

}
