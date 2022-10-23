package handson.exercises.impl;

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
                null;
    }

}
