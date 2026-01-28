package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.shipping_method.ShippingMethod;
import com.commercetools.api.models.shipping_method.ShippingMethodPagedQueryResponse;
import com.fasterxml.jackson.databind.JsonNode;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ShippingService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<ShippingMethodPagedQueryResponse>> getShippingMethods() {
        return apiRoot
                .shippingMethods()
                .get()
                .withExpand("zoneRates[*].zone")
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<ShippingMethod>> getShippingMethodByKey(final String key) {
        return apiRoot
                .shippingMethods()
                .withKey(key)
                .get()
                .withExpand("zoneRates[*].zone")
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<ShippingMethodPagedQueryResponse>> getShippingMethodsByCountry(final String countryCode) {
        return apiRoot
                .shippingMethods()
                .matchingLocation()
                .get()
                .addCountry(countryCode)
                .withExpand("zoneRates[*].zone")
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<ShippingMethodPagedQueryResponse>> getShippingMethodsByInStoreCart(
            final String storeKey,
            final String cartId) {
        return apiRoot
                .inStore(storeKey)
                .shippingMethods()
                .matchingCart()
                .get()
                .withCartId(cartId)
                .withExpand("zoneRates[*].zone")
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<JsonNode>> checkShippingMethodExistence(final String key) {
        return apiRoot
                .shippingMethods()
                .withKey(key)
                .head()
                .execute();
    }
}
