package prep;

import com.commercetools.api.client.ProjectApiRoot;
import prep.impl.ApiPrefixHelper;
import prep.impl.CartService;
import prep.impl.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static prep.impl.ClientService.createApiClient;


public class PrepTask1b_CREATE_CARTS {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Create a cart
        // Create an anonymous cart

        Logger logger = LoggerFactory.getLogger(PrepTask1b_CREATE_CARTS.class.getName());

        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        CustomerService customerService = new CustomerService(apiRoot_poc);
        CartService cartService = new CartService(apiRoot_poc);

        // TODO Step 1: Create a cart for the customer
        // TODO Add Line Items to it
        // TODO Copy the cart ID
        logger.info("Cart created: " +
                ""
        );

        apiRoot_poc.close();
    }
}
