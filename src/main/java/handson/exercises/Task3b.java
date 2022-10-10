package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.subscription.ChangeSubscriptionBuilder;
import com.commercetools.api.models.subscription.GoogleCloudPubSubDestinationBuilder;
import com.commercetools.api.models.subscription.SubscriptionDraftBuilder;
import handson.solutions.impl.ApiPrefixHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


public class Task3b {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Subscriptions

        Logger logger = LoggerFactory.getLogger(Task3b.class.getName());

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
