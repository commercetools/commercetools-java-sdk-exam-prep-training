package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import handson.exercises.impl.CustomerGroupService;
import handson.exercises.impl.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static prep.impl.ClientService.createApiClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class Task1a_CRUD {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Api Clients
        // Get, Post

        Logger logger = LoggerFactory.getLogger("commercetools");

        // TODO Step 1: Provide names
        String customerGroupName = "coolbuyers";
        String customerGroupKey = "coolbuyers-customer-group";
        String customerEmail = "michael@example.com";
        String customerPassword = "password";
        String customerKey = "customer-michael";
        String customerFirstName = "michael";
        String customerLastName = "tester";
        String customerCountry = "DE";

        // Create an admin api client for your own project
        // TODO Step 2: Provide credentials in dev.properties
        // TODO Step 3: Check ClientService.java

        final ProjectApiRoot apiRoot = createApiClient("ctp");
        CustomerService customerService = new CustomerService(apiRoot);
        CustomerGroupService customerGroupService = new CustomerGroupService(apiRoot);

        // Create a customer group
        // TODO Step 4: Create a customer group in CustomerGroupService.createCustomerGroup
        logger.info("Customer group created: " +
                customerGroupService.createCustomerGroup(
                                customerGroupName,
                                customerGroupKey
                        )
                        .get()
                        .getBody().getName()
        );


        // Create a customer, Verify the customer
        // Get the customer group
        // Assign the customer to your group
        // TODO Step 5: Call CustomerService.createCustomer
        logger.info("Customer created: " +
                ""
        );

        apiRoot.close();
    }
}
