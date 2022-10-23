package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import handson.solutions.impl.ApiPrefixHelper;
import handson.solutions.impl.CustomerGroupService;
import handson.solutions.impl.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


public class Task1a_CRUD {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Api Clients
        // Get, Post

        Logger logger = LoggerFactory.getLogger(Task1a_CRUD.class.getName());

        // TODO Step 1: Provide names
        String customerGroupName = "coolbuyers";
        String customerGroupKey = "coolbuyers-custom-group";
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
        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        CustomerService customerService = new CustomerService(apiRoot_poc);
        CustomerGroupService customerGroupService = new CustomerGroupService(apiRoot_poc);

        // Create a customer group
        // TODO Strep 5: Create a customer group in CustomerGroupService.java
        logger.info("Customer created: " +
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
                customerService.createCustomer(
                                customerEmail,
                                customerPassword,
                                customerKey,
                                customerFirstName,
                                customerLastName,
                                customerCountry
                        )
                        .thenComposeAsync(signInResult -> customerService.createEmailVerificationToken(signInResult, 5))
                        .thenComposeAsync(customerService::verifyEmail)
                        .thenCombineAsync(customerGroupService.getCustomerGroupByKey(customerGroupKey),
                                customerService::assignCustomerToCustomerGroup)
                        .get()
                        .get()
                        .getBody().getKey()
        );

        apiRoot_poc.close();
    }
}
