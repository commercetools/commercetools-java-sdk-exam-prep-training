package handson.notUsed;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.channel.Channel;
import com.commercetools.api.models.state.State;
import handson.solutions.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


/**
 * Create a cart for a customer, add a product to it, create an order from the cart and change the order state.
 *
 * See:
 */
public class Task04b_CHECKOUT {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        final String apiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();

        final ProjectApiRoot client = createApiClient(apiClientPrefix);

        CustomerService customerService = new CustomerService(client);
        CartService cartService = new CartService(client);
        OrderService orderService = new OrderService(client);
        PaymentService paymentService = new PaymentService(client);
        Logger logger = LoggerFactory.getLogger(Task04b_CHECKOUT.class.getName());


        // TODO: Fetch a channel if your inventory mode will not be NONE
        //
        Channel channel = client
                .channels()
                .get()
                .withWhere("key=" + "\"" + "berlin-warehouse" + "\"")                          // See also: .addWhere
                .execute()
                .toCompletableFuture().get()
                .getBody().getResults().get(0);

        final State state = client
                .states()
                .withKey("mhOrderPacked")
                .get()
                .execute()
                .toCompletableFuture().get()
                .getBody();



        // TODO: Perform cart operations:
        //      Get Customer, create cart, add products, add inventory mode
        //      add discount codes, perform a recalculation
        // TODO: Convert cart into an order, set order status, set state in custom work
        //
        // TODO: add payment
        // TAKE CARE: Take off payment for second or third try OR change the interfaceID with a timestamp
        //
        // TODO additionally: add custom line items, add shipping method
        //

        /*
        logger.info("Created cart/order ID: " +
                customerService.getCustomerByKey("customer-michael15")
                        .thenComposeAsync(cartService::createCart)

                        .thenComposeAsync(cartApiHttpResponse -> cartService.addProductToCartBySkusAndChannel(
                                cartApiHttpResponse,
                                channel,
                                "TULIPSEED01", "TULIPSEED01", "TULIPSEED02")
                        )

                        .thenComposeAsync(cartApiHttpResponse -> cartService.addDiscountToCart(cartApiHttpResponse,"MIXED"))
                        .thenComposeAsync(cartService::recalculate)
                        .thenComposeAsync(cartService::setShipping)

                        .thenComposeAsync(cartApiHttpResponse -> paymentService.createPaymentAndAddToCart(
                                cartApiHttpResponse,
                                "We_Do_Payments",
                                "CREDIT_CARD",
                                "we_pay_73636" + Math.random(),                // Must be unique.
                                "pay82626"+ Math.random())                    // Must be unique.
                        )

                        .thenComposeAsync(orderService::createOrder)
                        .thenComposeAsync(orderApiHttpResponse -> orderService.changeState(orderApiHttpResponse, OrderState.COMPLETE))
                        .thenComposeAsync(orderApiHttpResponse -> orderService.changeWorkflowState(orderApiHttpResponse, state))

                        .toCompletableFuture().get()
                        .getBody().getId()
        );

*/

        client.close();
    }
}
