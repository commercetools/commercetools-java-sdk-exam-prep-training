package handson.solutions.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.custom_object.CustomObjectDraftBuilder;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.type.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vrap.rmf.base.client.ApiHttpResponse;

import javax.json.JsonObject;
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
                apiRoot
                        .types()
                        .get()
                        .addWhere("resourceTypeIds contains any (\"customer\")")    // you want rather an SQL-like, here we have it
                        .execute();
    }


    public CompletableFuture<ApiHttpResponse<CustomObject>> getCustomObject(
            String container,
            String key
    ) {
        return
                apiRoot
                        .customObjects()
                        .withContainerAndKey(container, key)
                        .get()
                        .execute();
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

    public CompletableFuture<ApiHttpResponse<CustomObject>> createCustomObject(
            String container,
            String key,
            JsonObject jsonData
    ) throws JsonProcessingException {

        return
                apiRoot
                        .customObjects()
                        .post(
                                CustomObjectDraftBuilder.of()
                                        .container(container)
                                        .key(key)
                                        .value(
                                                new ObjectMapper()
                                                        .readTree(jsonData.toString()))
                                        .build()
                        )
                        .execute();
    }

}
