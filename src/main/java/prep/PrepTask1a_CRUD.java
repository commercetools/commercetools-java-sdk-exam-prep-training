package prep;

import com.commercetools.api.client.ProjectApiRoot;
import prep.impl.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static prep.impl.ClientService.createApiClient;


public class PrepTask1a_CRUD {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Api Clients
        // Get, Post

        Logger logger = LoggerFactory.getLogger("commercetools");

        // TODO Step 1: Create an admin api client for your own project
        // TODO Step 2: Provide credentials in dev.properties
        // TODO Step 4: Check impl/ClientService and create an apiRoot

        final ProjectApiRoot apiRoot = createApiClient("ctp");

        // TODO Step 5: Query project

        // TODO Step 6: Query tax categories

        // TODO Step 7: Get a tax category by key

        // TODO Step 8: Create a new customer

        CustomerService customerService = new CustomerService(apiRoot);

//        logger.info("Create sign up completed."  +
//                customerService.createCustomer("","","","","",""));

        // TODO Step 9: Create a customer group

        // TODO Step 10: Assign the customer to the customer group

        apiRoot.close();
    }
}
