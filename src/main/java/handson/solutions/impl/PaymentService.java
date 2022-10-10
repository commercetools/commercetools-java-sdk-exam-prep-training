package handson.solutions.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartAddPaymentActionBuilder;
import com.commercetools.api.models.cart.CartUpdateBuilder;
import com.commercetools.api.models.common.MoneyBuilder;
import com.commercetools.api.models.payment.*;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;


// TODO: Change order of actions
// a) Create Payment
// b) Set Payment on card
// c) Add Transactions

// TODO: Add Interface actions
// d) Requires the declaration of custom fields (taught in a later session in class)

// TODO: Allow the customer to play with different payment methods
// Have an order number like order1234-12, where -12 is not visible to the customer

public class PaymentService {

    final ProjectApiRoot apiRoot;

    public PaymentService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

    public CompletableFuture<ApiHttpResponse<Cart>> createPaymentAndAddToCart(
            final ApiHttpResponse<Cart> cartApiHttpResponse,
            String psp_Name,
            String psp_Method,
            String interfaceId,
            String interactionId) {

        final Cart cart = cartApiHttpResponse.getBody();

        return
                apiRoot
                        .payments()
                        .post(
                                PaymentDraftBuilder.of()
                                        .amountPlanned(
                                                MoneyBuilder.of()
                                                    .centAmount(cart.getTotalPrice().getCentAmount())
                                                    .currencyCode(cart.getTotalPrice().getCurrencyCode())
                                                    .build()
                                        )
                                        .paymentMethodInfo(
                                                PaymentMethodInfoBuilder.of()
                                                        .paymentInterface(psp_Name)        // PSP Provider Name: WireCard, ....
                                                        .method(psp_Method)                // PSP Provider Method: CreditCard
                                                        .build())
                                        .interfaceId(interfaceId)                          // ID of the payment, important !!!
                                        .build()
                        )
                        .execute()
                        .thenComposeAsync(paymentApiHttpResponse ->
                                    apiRoot
                                            .payments()
                                            .withId(paymentApiHttpResponse.getBody().getId())
                                            .post(
                                                    PaymentUpdateBuilder.of()
                                                            .version(paymentApiHttpResponse.getBody().getVersion())
                                                            .actions(
                                                                PaymentAddTransactionActionBuilder.of()
                                                                        .transaction(
                                                                                TransactionDraftBuilder.of()
                                                                                        .amount(
                                                                                                MoneyBuilder.of()
                                                                                                        .centAmount(cart.getTotalPrice().getCentAmount())
                                                                                                        .currencyCode(cart.getTotalPrice().getCurrencyCode())
                                                                                                        .build()
                                                                                        )
                                                                                    .timestamp(ZonedDateTime.now())
                                                                                    .type(TransactionType.CHARGE)
                                                                                    .interactionId(interactionId)
                                                                                    .build()
                                                                        )
                                                                        .build(),

                                                                // PaymentAddInterfaceInteractionActionBuilder.of()                 // Requires custom fields

                                                                PaymentSetStatusInterfaceCodeActionBuilder.of()
                                                                        .interfaceCode("SUCCESS")
                                                                        .build(),
                                                                PaymentSetStatusInterfaceTextActionBuilder.of()
                                                                        .interfaceText("We got the money.")
                                                                        .build()
                                                            )
                                                            .build()
                                            )
                                            .execute()
                                            )
                        .thenComposeAsync(paymentApiHttpResponse ->
                                    apiRoot
                                            .carts()
                                            .withId(cart.getId())
                                            .post(
                                                    CartUpdateBuilder.of()
                                                            .version(cart.getVersion())
                                                            .actions(
                                                                CartAddPaymentActionBuilder.of()
                                                                    .payment(
                                                                            PaymentResourceIdentifierBuilder.of()
                                                                                .id(paymentApiHttpResponse.getBody().getId())
                                                                                .build()
                                                                    )
                                                                    .build()
                                                            )
                                                            .build()
                                            )
                                            .execute()
                                );
    }

}
