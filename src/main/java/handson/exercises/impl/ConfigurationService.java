package handson.exercises.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.type.*;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides operations to work with {@link Customer}s.
 */
public class ConfigurationService {

    final ProjectApiRoot apiRoot;

    public ConfigurationService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

    // TODO
    // resourceTypeIds contains any ("customer")
    public CompletableFuture<ApiHttpResponse<TypePagedQueryResponse>> getCustomTypes() {

        return
                null;
    }


    public CompletableFuture<ApiHttpResponse<Type>> createCustomType(
            String key,
            LocalizedString name,
            ResourceTypeId assignedType,
            List<FieldDefinition> fieldDefinitions
    ) {

        return
                apiRoot
                        .types()
                        .post(TypeDraftBuilder.of()
                                .key(key)
                                .name(name)
                                .resourceTypeIds(assignedType)
                                .fieldDefinitions(fieldDefinitions)
                                .build()
                        )
                        .execute();
    }


}
