package handson.solutions.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_type.AttributeDefinitionDraftBuilder;
import com.commercetools.api.models.product_type.ProductType;
import com.commercetools.api.models.product_type.ProductTypeDraftBuilder;
import com.commercetools.api.models.product_type.ProductTypePagedQueryResponse;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<ApiHttpResponse<ProductType>> transferProductType(ProductType productType) {

        return
                apiRoot
                        .productTypes()
                        .post(ProductTypeDraftBuilder.of()
                                .attributes(Arrays.asList(
                                        AttributeDefinitionDraftBuilder.of()
                                                .name(productType.getAttributes().get(0).getName())
                                                .label(productType.getAttributes().get(0).getLabel())
                                                .inputTip(productType.getAttributes().get(0).getInputTip())
                                                .isRequired(productType.getAttributes().get(0).getIsRequired())
                                                .isSearchable(productType.getAttributes().get(0).getIsSearchable())
                                                .inputHint(productType.getAttributes().get(0).getInputHint())
                                                .attributeConstraint(productType.getAttributes().get(0).getAttributeConstraint())
                                                .type(productType.getAttributes().get(0).getType())
                                                .build()
                                ))              // would have to stream and convert to list if more
                                .description(productType.getDescription())
                                .key(productType.getKey())
                                .name(productType.getName())
                                .build()
                        )
                        .execute();
    }

}
