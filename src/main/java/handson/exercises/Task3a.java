package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.customer.Customer;
import handson.exercises.impl.ApiPrefixHelper;
import handson.exercises.impl.CartService;
import handson.exercises.impl.CustomObjectService;
import handson.exercises.impl.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


public class Task3a {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Processes and API Extensions
        // GraphQL

        Logger logger = LoggerFactory.getLogger(Task3a.class.getName());

        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        CustomerService customerService = new CustomerService(apiRoot_poc);
        CartService cartService = new CartService(apiRoot_poc);
        CustomObjectService customObjectService = new CustomObjectService(apiRoot_poc);

       // TODO Step 1: Provide cart and customer
        //
        String cartId = "7a6c71c3-a56c-4883-910b-7efc4e42ce37";
        String customerId = "a9c7b8ef-3ec9-4bec-8c55-17347d7268c8";
        String customObjectContainer = "BonusPointCalculationSchema";
        String customObjectKey = "allCartValues";
        String projectKey = "training-exam-prep-exercise-01";

        // TODO Step 1: Customer wants to create an order, get all data to update his/her bonus points
        //
        final Cart cart = null;
        final Customer customer = null;
        final CustomObject customObject = null;

        // Log custom object
        logger.info("Custom Object retrieved: " + customObject.getValue().toString());

        // Calculate new bonus fields for customer
        // Update custom field on customer
        // Add custom line item on cart
        // convert to order
        //
        // Continue in Task3a_Improved

        apiRoot_poc.close();
    }
}
