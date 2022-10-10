package handson.solutions.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.commercetools.api.models.customer_group.CustomerGroupDraftBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.concurrent.CompletableFuture;

/**
 * This class provides operations to work with {@link Customer}s.
 */
public class CustomerGroupService {

    final ProjectApiRoot apiRoot;

    public CustomerGroupService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

    public CompletableFuture<ApiHttpResponse<CustomerGroup>> createCustomerGroup(
            final String customerGroupName,
            final String customerGroupKey) {

        return
                apiRoot
                        .customerGroups()
                        .post(CustomerGroupDraftBuilder.of()
                                .key(customerGroupKey)
                                .groupName(customerGroupName)
                                .build()
                        )
                        .execute();
    }

    public CompletableFuture<ApiHttpResponse<CustomerGroup>> getCustomerGroupByKey(
            final String customerGroupKey) {

        return
                apiRoot
                        .customerGroups()
                        .withKey(customerGroupKey)
                        .get()
                        .execute();
    }

}
