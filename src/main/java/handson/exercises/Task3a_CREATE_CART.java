package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import handson.exercises.impl.CartService;
import static handson.exercises.impl.ClientService.createApiClient;
import handson.exercises.impl.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Task3a_CREATE_CART {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Create a cart
        // Add Line Items

        Logger logger = LoggerFactory.getLogger("commercetools");

        final ProjectApiRoot apiRoot = createApiClient("ctp");
        CustomerService customerService = new CustomerService(apiRoot);
        CartService cartService = new CartService(apiRoot);

        String customerKey = "customer-michael";

        // TODO Step 1: Create a cart for the customer
        // TODO Add Line Items to it
        // TODO Copy the cart ID
        logger.info("Cart created: " +
                ""
        );

        apiRoot.close();
    }
}
