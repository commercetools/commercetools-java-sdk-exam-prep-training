package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import static handson.exercises.impl.ClientService.createApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class Task4_SUBSCRIPTIONS {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Subscriptions

        Logger logger = LoggerFactory.getLogger("commercetools");

        final ProjectApiRoot apiRoot = createApiClient("poc");

        // TODO Step 1: Provide subscription key
        //
        String subscriptionKey = "customer-change-subscription";

        // TODO Step 2
        // Add subscription on customer change for watching bonus points change
        logger.info("Created subscription: " +
            ""
        );

        apiRoot.close();
    }
}
