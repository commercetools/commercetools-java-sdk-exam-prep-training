package prep.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.customer.Customer;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.concurrent.CompletableFuture;

/**

 */
public class CartService {

    final ProjectApiRoot apiRoot;

    public CartService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

    public CompletableFuture<ApiHttpResponse<Cart>> getCartById(
            final String cartId) {

        return
                null;
    }


    public CompletableFuture<ApiHttpResponse<Cart>> createCart(final ApiHttpResponse<Customer> customerApiHttpResponse) {

        return
                null;
    }



    public CompletableFuture<ApiHttpResponse<Cart>> addProductToCartBySkusAndChannel(
            final ApiHttpResponse<Cart> cartApiHttpResponse,
            final String ... skus) {

        return
                null;
    }



    public CompletableFuture<ApiHttpResponse<Cart>> setShipping(final ApiHttpResponse<Cart> cartApiHttpResponse) {

        return
                null;
    }


    public CompletableFuture<ApiHttpResponse<Cart>> addCustomLineItem(
            final ApiHttpResponse<Cart> cartApiHttpResponse,
            final String text,
            final long centAmount,
            final String slug,
            final String taxCategoryKey) {

        return
                null;
    }
}
