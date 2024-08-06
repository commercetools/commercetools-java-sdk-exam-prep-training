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
        String customerEmail = "michael15@example.com";
        String customerPassword = "password";
        String customerKey = "customer-michael15";
        String customerFirstName = "michael";
        String customerLastName = "tester";
        String customerCountry = "DE";

        // Create an admin api client for your own project
        // TODO Step 2: Provide credentials in dev.properties
        // TODO Step 3: Provide prefix in APIHelper
        // TODO Step 4: Check ClientService.java

        final ProjectApiRoot apiRoot = createApiClient("poc");
        CustomerService customerService = new CustomerService(apiRoot);
        CustomerGroupService customerGroupService = new CustomerGroupService(apiRoot);

        // Create a customer group
        // TODO Step 5: Create a customer group in CustomerGroupService.java
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
        // TODO Step 6: Call CustomerService.class, CustomerGroupService.class
        logger.info("Customer created: " +
                ""
        );

        apiRoot.close();
    }
}
