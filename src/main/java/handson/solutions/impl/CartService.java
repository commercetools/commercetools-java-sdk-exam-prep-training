package handson.solutions.impl;

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


    public CompletableFuture<ApiHttpResponse<Cart>> createCart(final ApiHttpResponse<Customer> customerApiHttpResponse) {

        final Customer customer = customerApiHttpResponse.getBody();
        return
                apiRoot.carts()
                        .post(
                                CartDraftBuilder.of()
                                        .currency("EUR")
                                        .customerEmail(customer.getEmail())
                                        .customerId(customer.getId())
                                        .shippingAddress(
                                                customer.getAddresses().stream()
                                                        .filter(address -> address.getId().equals(customer.getDefaultShippingAddressId()))
                                                        .findFirst()
                                                        .orElse(null)
                                        )
                                        .country(customer.getAddresses().stream()
                                                .filter(address -> address.getId().equals(customer.getDefaultShippingAddressId()))
                                                .findFirst()
                                                .orElse(null).getCountry()
                                        )
                                        .build()
                        )
                        .execute();
    }



    public CompletableFuture<ApiHttpResponse<Cart>> addProductToCartBySkusAndChannel(
            final ApiHttpResponse<Cart> cartApiHttpResponse,
            final String ... skus) {

        final Cart cart = cartApiHttpResponse.getBody();
        return
                apiRoot.carts()
                        .withId(cart.getId())
                        .post(
                                CartUpdateBuilder.of()
                                        .version(cart.getVersion())
                                        .actions(
                                                Stream.of(skus)
                                                        .map(sku -> CartAddLineItemActionBuilder.of()
                                                                .sku(sku)
                                                                .build()
                                                        )
                                                        .collect(Collectors.toList())
                                        )
                                        .build()
                        )
                        .execute();
    }



    public CompletableFuture<ApiHttpResponse<Cart>> setShipping(final ApiHttpResponse<Cart> cartApiHttpResponse) {

        final Cart cart = cartApiHttpResponse.getBody();
        final ShippingMethod shippingMethod = apiRoot.shippingMethods()
                .get()
                .executeBlocking()
                .getBody()
                .getResults().get(0);

        return
                apiRoot
                        .carts()
                        .withId(cart.getId())
                        .post(
                                CartUpdateBuilder.of()
                                        .version(cart.getVersion())
                                        .actions(
                                                CartSetShippingMethodActionBuilder.of()
                                                        .shippingMethod(ShippingMethodResourceIdentifierBuilder.of()
                                                                .id(shippingMethod.getId())
                                                                .build())
                                                        .build()
                                        )
                                        .build()
                        )
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
                                                        .quantity(1L)
                                                        .name(LocalizedStringBuilder.of()
                                                                .addValue("en", text)
                                                                .addValue("de", text)
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
