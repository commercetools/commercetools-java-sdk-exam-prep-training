package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.subscription.*;
import static handson.solutions.impl.ClientService.createApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Task4_SUBSCRIPTIONS {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Logger logger = LoggerFactory.getLogger("commercetools");

        final ProjectApiRoot apiRoot = createApiClient("ctp");

        // Provide subscription key
        //
        String subscriptionKey = "customer-change-subscription-AWS";

        // Add subscription on customer change for watching bonus points change
        logger.info("Created subscription: " +
            apiRoot
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
//                          //SQS Example with "Credentials" authentication mode
//                            SqsDestinationBuilder.of()
//                                .queueUrl("https://sqs.us-east-2.amazonaws.com/015253045234/training_customer_change_queue")
//                                .region("us-east-2")
//                                .accessKey("")
//                                .accessSecret("")
//                                .build()
                            SqsDestinationBuilder.of()
                                    .authenticationMode(AwsAuthenticationMode.IAM)
                                    .queueUrl("https://sqs.us-east-2.amazonaws.com/015253045234/training_customer_change_queue")
                                    .region("us-east-2")
                                    .build()
                        )
                        .changes(
                            ChangeSubscriptionBuilder.of()
                                .resourceTypeId(ChangeSubscriptionResourceTypeId.CUSTOMER) // https://docs.commercetools.com/api/types#referencetype
                                .build()
                        )
                        .build()
                )
                .execute()
                .get()
                .getBody().getKey()
        );

        apiRoot.close();
    }
}
