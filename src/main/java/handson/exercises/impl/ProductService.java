package handson.exercises.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.ProductPagedQueryResponse;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.concurrent.CompletableFuture;

/**

 */
public class ProductService {

    final ProjectApiRoot apiRoot;

    public ProductService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

    public CompletableFuture<ApiHttpResponse<ProductPagedQueryResponse>> getProducts() {
        return
                apiRoot
                        .products()
                        .get()
                        .execute();
    }


}
