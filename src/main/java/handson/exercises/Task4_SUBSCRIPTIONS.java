package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import handson.exercises.impl.ApiPrefixHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


public class Task4_SUBSCRIPTIONS {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Subscriptions

        Logger logger = LoggerFactory.getLogger(Task4_SUBSCRIPTIONS.class.getName());

        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );

        // TODO Step 1: Provide subscription key
        //
        String subscriptionKey = "customer-change-subscription-AWS";

        // TODO Step 2
        // Add subscription on customer change for watching bonus points change
        logger.info("Created subscription: " +
            ""
        );

        apiRoot_poc.close();
    }
}
