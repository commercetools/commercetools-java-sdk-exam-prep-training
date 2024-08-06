package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import handson.solutions.impl.CustomerService;
import handson.solutions.impl.CartService;
import static handson.solutions.impl.ClientService.createApiClient;
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

        String customerKey = "nd-customer";

        // Create a cart for the customer
        // Add Line Items to it
        //
        logger.info("Cart created: " + customerService.getCustomerByKey(customerKey)
                .thenComposeAsync(cartService::createCart)
                .thenComposeAsync(cartApiHttpResponse -> cartService.addProductToCartBySkusAndChannel(cartApiHttpResponse,"rose-flowers-box"))
                .get().getBody().getId());

        apiRoot.close();
    }
}
