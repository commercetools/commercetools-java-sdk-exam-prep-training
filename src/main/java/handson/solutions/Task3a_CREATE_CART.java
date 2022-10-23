package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import handson.solutions.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


public class Task3a_CREATE_CART {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Create a cart
        // Add Line Items

        Logger logger = LoggerFactory.getLogger(Task3a_CREATE_CART.class.getName());

        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        CustomerService customerService = new CustomerService(apiRoot_poc);
        CartService cartService = new CartService(apiRoot_poc);

        String customerKey = "customer-michael15";

        // TODO Step 1: Create a cart for the customer
        // TODO Add Line Items to it
        //
        logger.info("Cart created: " + customerService.getCustomerByKey(customerKey)
                .thenComposeAsync(cartService::createCart)
                .thenComposeAsync(cartApiHttpResponse -> cartService.addProductToCartBySkusAndChannel(cartApiHttpResponse,"tulip-seed-box"))
                .get().getBody().getId());

        apiRoot_poc.close();
    }
}
