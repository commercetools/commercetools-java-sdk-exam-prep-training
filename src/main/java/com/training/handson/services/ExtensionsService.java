package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.type.*;
import com.training.handson.dto.CustomObjectRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ExtensionsService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<Type>> createType() {
        // Define labels for the fields
        Map<String, String> labelsForFieldPoints = new HashMap<String, String>() {{
            put("de-DE", "Points");
            put("en-US", "Points");
        }};
        Map<String, String> labelsForFieldSchemeName = new HashMap<String, String>() {{
            put("de-DE", "Scheme Name");
            put("en-US", "Scheme Name");
        }};

        // Define the fields
        List<FieldDefinition> definitions = Arrays.asList(
                FieldDefinitionBuilder.of()
                        .name("points")
                        .required(true)
                        .label(lsb -> lsb.values(labelsForFieldPoints))
                        .type(CustomFieldNumberType.of())
                        .build(),
                FieldDefinitionBuilder.of()
                        .name("scheme-name")
                        .required(false)
                        .label(lsb -> lsb.values(labelsForFieldSchemeName))
                        .type(CustomFieldStringType.of())
                        .build()
        );

        // Define the name for the type
        Map<String, String> nameForType = new HashMap<String, String>() {{
            put("de-DE", "CT Loyalty Scheme");
            put("en-US", "CT Loyalty Scheme");
        }};

        // Create the custom type asynchronously
        return apiRoot
                .types()
            .post(
                typeDraftBuilder -> typeDraftBuilder
                    .key("ct-loyalty-extension")
                    .name(lsb -> lsb.values(nameForType))
                    .resourceTypeIds(
                        ResourceTypeId.CUSTOMER,
                        ResourceTypeId.ORDER
                    )
                    .fieldDefinitions(definitions)
            ).execute();
    }

    public CompletableFuture<ApiHttpResponse<CustomObject>> createCustomObject(
            final CustomObjectRequest customObjectRequest) {

        return apiRoot.customObjects()
                .post(draftBuilder -> draftBuilder
                        .container("schemas")
                        .key("tt-loyalty-schema")
                        .value(customObjectRequest.getJson()))
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<CustomObject>> getCustomObjectWithContainerAndKey() {

        return apiRoot
                .customObjects()
                .withContainerAndKey("schemas", "tt-loyalty-schema")
                .get()
                .execute();
    }

}
