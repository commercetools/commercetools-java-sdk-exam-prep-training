package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.CartResourceIdentifierBuilder;
import com.commercetools.api.models.customer.*;
import com.commercetools.api.models.type.FieldContainer;
import com.commercetools.api.models.type.FieldContainerBuilder;
import com.training.handson.dto.CustomFieldRequest;
import com.training.handson.dto.CustomerLoginRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CustomerService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<Customer>> getCustomerByKey(
            final String storeKey,
            final String customerKey) {
        return apiRoot
                .inStore(storeKey)
                .customers()
                .withKey(customerKey)
                .get()
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<CustomerSignInResult>> loginCustomer(
            final String storeKey,
            final CustomerLoginRequest customerCreateRequest) {

        final String email = customerCreateRequest.getEmail();
        final String password = customerCreateRequest.getPassword();
        final String anonymousCartId = customerCreateRequest.getAnonymousCartId();

        CustomerSigninBuilder customerSigninBuilder = CustomerSigninBuilder.of()
                .email(email)
                .password(password);

        if (StringUtils.isNotEmpty(anonymousCartId)){
                customerSigninBuilder
                        .anonymousCart(CartResourceIdentifierBuilder.of()
                                .id(anonymousCartId)
                                .build()
                        );
        }

        return apiRoot
                .inStore(storeKey)
                .login()
                .post(customerSigninBuilder.build())
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<Customer>> updateCustomerCustomFields(
            final String storeKey,
            final String customerKey,
            final CustomFieldRequest customFieldRequest
            ){
        return getCustomerByKey(storeKey, customerKey)
                .thenApply(ApiHttpResponse::getBody)
                .thenCompose(customer -> {
                    CustomerUpdateAction customerUpdateAction = CustomerUpdateActionBuilder.of()
                            .setCustomTypeBuilder()
                            .type(typeResourceIdentifierBuilder ->
                                    typeResourceIdentifierBuilder
                                            .key("ct-loyalty-extension"))
                            .build();
                    return apiRoot.inStore(storeKey)
                            .customers()
                            .withKey(customerKey)
                            .post(customerUpdateBuilder ->
                                    customerUpdateBuilder
                                            .version(customer.getVersion())
                                            .actions(customerUpdateAction)
                            )
                            .execute();
                });
    }

}
