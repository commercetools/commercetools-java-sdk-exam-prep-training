package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import handson.solutions.impl.CustomerGroupService;
import handson.solutions.impl.CustomerService;
import static handson.solutions.impl.ClientService.createApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;




public class Task1a_CRUD {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Logger logger = LoggerFactory.getLogger("commercetools");

        String customerGroupName = "coolbuyers";
        String customerGroupKey = "coolbuyers-customer-group";
        String customerEmail = "nd@example.com";
        String customerPassword = "password";
        String customerKey = "nd-customer";
        String customerFirstName = "customer";
        String customerLastName = "tester";
        String customerCountry = "DE";

        // Create an admin api client for your own project
        final ProjectApiRoot apiRoot = createApiClient("ctp");
        CustomerService customerService = new CustomerService(apiRoot);
        CustomerGroupService customerGroupService = new CustomerGroupService(apiRoot);

        // Create Customer Group
        logger.info("Customer created: " +
                customerGroupService.createCustomerGroup(
                                customerGroupName,
                                customerGroupKey
                        )
                        .get()
                        .getBody().getName()
        );

        // Sign up a Customer and assign to the Customer Group
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

        apiRoot.close();
    }
}
