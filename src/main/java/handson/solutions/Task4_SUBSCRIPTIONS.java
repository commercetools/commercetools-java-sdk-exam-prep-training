package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.subscription.ChangeSubscriptionBuilder;
import com.commercetools.api.models.subscription.SqsDestinationBuilder;
import com.commercetools.api.models.subscription.SubscriptionDraftBuilder;
import handson.solutions.impl.ApiPrefixHelper;
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
            apiRoot_poc
                .subscriptions()
                .post(
                    SubscriptionDraftBuilder.of()
                        .key(subscriptionKey)
                        .destination(
                            //for GCP Pub/Sub topic
//                            GoogleCloudPubSubDestinationBuilder.of()
//                                    .projectId("ct-support")
//                                    .topic("training-subscription-sample")
//                                    .build()
//                                        // for AWS SQS Queue
                            SqsDestinationBuilder.of()
                                .queueUrl("https://sqs.us-east-2.amazonaws.com/015253045234/training_customer_change_queue")
                                .region("us-east-2")
                                .accessKey("")
                                .accessSecret("")
                                .build()
                        )
                        .changes(
                            ChangeSubscriptionBuilder.of()
                                .resourceTypeId("customer") // https://docs.commercetools.com/api/types#referencetype
                                .build()
                        )
                        .build()
                )
                .execute()
                .get()
                .getBody().getKey()
        );

        apiRoot_poc.close();
    }
}
