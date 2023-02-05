package handson.solutions.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_type.AttributeDefinitionDraftBuilder;
import com.commercetools.api.models.product_type.ProductType;
import com.commercetools.api.models.product_type.ProductTypeDraftBuilder;
import com.commercetools.api.models.product_type.ProductTypePagedQueryResponse;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**

 */
public class ProductTypeService {

    final ProjectApiRoot apiRoot;

    public ProductTypeService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

    public CompletableFuture<ApiHttpResponse<ProductTypePagedQueryResponse>> getProductTypes() {
        return
                apiRoot
                        .productTypes()
                        .get()
                        .execute();
    }

    public CompletableFuture<ApiHttpResponse<ProductType>> getProductTypeByKey(final String key) {
        return
                apiRoot
                        .productTypes()
                        .withKey(key)
                        .get()
                        .execute();
    }

    public CompletableFuture<ApiHttpResponse<ProductType>> transferProductType(final ProductType productType) {

        return
                apiRoot
                        .productTypes()
                        .post(ProductTypeDraftBuilder.of()
                                .attributes(
                                        productType.getAttributes().stream().map(attributeDefinition -> AttributeDefinitionDraftBuilder.of()
                                                .name(attributeDefinition.getName())
                                                .label(attributeDefinition.getLabel())
                                                .inputTip(attributeDefinition.getInputTip())
                                                .isRequired(attributeDefinition.getIsRequired())
                                                .isSearchable(attributeDefinition.getIsSearchable())
                                                .inputHint(attributeDefinition.getInputHint())
                                                .attributeConstraint(attributeDefinition.getAttributeConstraint())
                                                .type(attributeDefinition.getType())
                                                .build()
                                        ).collect(Collectors.toList())
                                )              // would have to stream and convert to list if more
                                .description(productType.getDescription())
                                .key(productType.getKey())
                                .name(productType.getName())
                                .build()
                        )
                        .execute();
    }

}
