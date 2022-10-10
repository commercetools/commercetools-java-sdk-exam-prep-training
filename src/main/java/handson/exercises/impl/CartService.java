package handson.exercises.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.channel.Channel;
import com.commercetools.api.models.channel.ChannelResourceIdentifierBuilder;
import com.commercetools.api.models.common.LocalizedStringBuilder;
import com.commercetools.api.models.common.MoneyBuilder;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.shipping_method.ShippingMethod;
import com.commercetools.api.models.shipping_method.ShippingMethodResourceIdentifierBuilder;
import com.commercetools.api.models.tax_category.TaxCategoryResourceIdentifierBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                apiRoot
                        .carts()
                        .withId(cartId)
                        .get()
                        .execute();
    }

    public CompletableFuture<ApiHttpResponse<Cart>> addCustomLineItem(
            final ApiHttpResponse<Cart> cartApiHttpResponse,
            final String text,
            final long centAmount,
            final String slug,
            final String taxCategoryKey) {

        final Cart cart = cartApiHttpResponse.getBody();

        return
                apiRoot
                        .carts()
                        .withId(cart.getId())
                        .post(
                                CartUpdateBuilder.of()
                                        .version(cart.getVersion())
                                        .actions(
                                                CartAddCustomLineItemActionBuilder.of()
                                                        .quantity(1l)
                                                        .name(LocalizedStringBuilder.of()
                                                                .addValue("en-US", text)
                                                                .addValue("de-DE", text)
                                                                .build())
                                                        .money(MoneyBuilder.of()
                                                                .centAmount(centAmount)
                                                                .currencyCode("EUR")
                                                                .build()
                                                        )
                                                        .taxCategory(TaxCategoryResourceIdentifierBuilder.of()
                                                                .key(taxCategoryKey)
                                                                .build())
                                                        .slug(slug)
                                                        .build()
                                        )
                                        .build()

                        )
                        .execute();
    }


}
