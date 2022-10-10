package handson.solutions.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartResourceIdentifierBuilder;
import com.commercetools.api.models.order.*;
import com.commercetools.api.models.state.State;
import com.commercetools.api.models.state.StateResourceIdentifierBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.concurrent.CompletableFuture;

/**
 * This class provides operations to work with {@link Order}s.
 */
public class OrderService {

    final ProjectApiRoot apiRoot;

    public OrderService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

    public CompletableFuture<ApiHttpResponse<Order>> createOrder(final ApiHttpResponse<Cart> cartApiHttpResponse) {

        final Cart cart = cartApiHttpResponse.getBody();

        return
                apiRoot
                        .orders()
                        .post(
                                OrderFromCartDraftBuilder.of()
                                    .cart(CartResourceIdentifierBuilder.of()
                                            .id(cart.getId())
                                            .build())
                                    .version(cart.getVersion())
                                    .build()

                        )
                        .execute();
    }


    public CompletableFuture<ApiHttpResponse<Order>> changeState(
            final ApiHttpResponse<Order> orderApiHttpResponse,
            final OrderState state) {

        Order order = orderApiHttpResponse.getBody();

        return
                apiRoot
                        .orders()
                        .withId(order.getId())
                        .post(
                                OrderUpdateBuilder.of()
                                    .version(order.getVersion())
                                    .actions(
                                        OrderChangeOrderStateActionBuilder.of()
                                            .orderState(state)
                                            .build()
                                    )
                                    .build()
                        )
                        .execute();
    }


    public CompletableFuture<ApiHttpResponse<Order>> changeWorkflowState(
            final ApiHttpResponse<Order> orderApiHttpResponse,
            final State workflowState) {

        Order order = orderApiHttpResponse.getBody();

        return
                apiRoot
                        .orders()
                        .withId(order.getId())
                        .post(
                                OrderUpdateBuilder.of()
                                        .version(order.getVersion())
                                        .actions(
                                            OrderTransitionStateActionBuilder.of()
                                                .state(
                                                        StateResourceIdentifierBuilder.of()
                                                            .id(workflowState.getId())
                                                        .build()
                                                )
                                                .build()
                                        )
                                        .build()
                        )
                        .execute();
    }

}
