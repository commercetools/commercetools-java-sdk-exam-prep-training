package handson.exercises.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.AddressBuilder;
import com.commercetools.api.models.customer.*;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.commercetools.api.models.customer_group.CustomerGroupResourceIdentifierBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.concurrent.CompletableFuture;

/**
 * This class provides operations to work with {@link Customer}s.
 */
public class CustomerService {

    final ProjectApiRoot apiRoot;

    public CustomerService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

        public CompletableFuture<ApiHttpResponse<Customer>> getCustomerById(
                String customerId
        )
        {
            return
                    apiRoot
                            .customers()
                            .withId(customerId)
                            .get()
                            .execute();
        }

        public CompletableFuture<ApiHttpResponse<Customer>> getCustomerByKey(
                String customerKey
        )
        {
            return
                    apiRoot
                            .customers()
                            .withKey(customerKey)
                            .get()
                            .execute();
        }


        public CompletableFuture<ApiHttpResponse<CustomerSignInResult>> createCustomer(
        final String email,
        final String password,
        final String customerKey,
        final String firstName,
        final String lastName,
        final String country) {

        return apiRoot
                        .customers()
                        .post(CustomerDraftBuilder.of()
                                .email(email)
                                .password(password)
                                .firstName(firstName)
                                .lastName(lastName)
                                .key(customerKey)
                                .addresses(
                                    AddressBuilder.of()
                                            .key(customerKey + "-" + country)
                                            .country(country)
                                            .build()
                                )
                                .defaultShippingAddress(0)
                                .build())
                        .execute();

    }

    public CompletableFuture<ApiHttpResponse<CustomerToken>> createEmailVerificationToken(
            final ApiHttpResponse<CustomerSignInResult> customerSignInResultApiHttpResponse,
            final long timeToLiveInMinutes
    ) {

        final Customer customer = customerSignInResultApiHttpResponse.getBody().getCustomer();

        return
                apiRoot
                        .customers()
                        .emailToken()
                        .post(
                                CustomerCreateEmailTokenBuilder.of()
                                        .id(customer.getId())
                                        .ttlMinutes(timeToLiveInMinutes)
                                        .build()
                        )
                        .execute();
    }

       public CompletableFuture<ApiHttpResponse<Customer>> verifyEmail(final ApiHttpResponse<CustomerToken> customerTokenApiHttpResponse) {

        final CustomerToken customerToken = customerTokenApiHttpResponse.getBody();

        return
                apiRoot
                        .customers()
                        .emailConfirm()
                        .post(
                                CustomerEmailVerifyBuilder.of()
                                        .tokenValue(customerToken.getValue())
                                        .build()
                        )
                        .execute();
    }

    public CompletableFuture<ApiHttpResponse<Customer>> assignCustomerToCustomerGroup(
            final ApiHttpResponse<Customer> customerApiHttpResponse,
            final ApiHttpResponse<CustomerGroup> customerGroupApiHttpResponse) {

        final Customer customer = customerApiHttpResponse.getBody();
        final CustomerGroup customerGroup = customerGroupApiHttpResponse.getBody();

        return
                null;
    }

    public CompletableFuture<ApiHttpResponse<Customer>> setCustomFieldValue(
            final ApiHttpResponse<Customer> customerApiHttpResponse,
            final String customFieldName,
            final int customFieldValue
    ) {
        final Customer customer = customerApiHttpResponse.getBody();

        return
                apiRoot
                        .customers()
                        .withKey(customer.getKey())
                        .post(CustomerUpdateBuilder.of()
                                .version(customer.getVersion())
                                .actions(
                                        CustomerSetCustomFieldActionBuilder.of()
                                                .name(customFieldName)
                                                .value(customFieldValue)
                                                .build()
                                )
                                .build()
                        )
                        .execute();
    }


}
