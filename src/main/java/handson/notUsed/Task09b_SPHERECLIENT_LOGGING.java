package handson.notUsed;


import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import com.commercetools.api.models.customer.CustomerSetFirstNameActionBuilder;
import com.commercetools.api.models.customer.CustomerSetLastNameActionBuilder;
import com.commercetools.api.models.customer.CustomerUpdateBuilder;
import handson.solutions.impl.ApiPrefixHelper;
import io.vrap.rmf.base.client.ApiHttpException;
import io.vrap.rmf.base.client.ApiHttpHeaders;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.*;

public class Task09b_SPHERECLIENT_LOGGING {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        final String apiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();

        final String projectKey = getProjectKey(apiClientPrefix);
        final ProjectApiRoot client = createApiClient(apiClientPrefix);
        final String clientId = getClientId(apiClientPrefix);
        final String clientSecret = getClientSecret(apiClientPrefix);
        Logger logger = LoggerFactory.getLogger(Task09b_SPHERECLIENT_LOGGING.class.getName());

        // TODO 1..5
        //  Execute, inspect individually
        //

        // 1: Logging
        //      Modify logback.xml
        //      Inspect log output, header information, etc.


        // 2: ReUse tokens
        //      See Task06c_GRAPHQL_Nodes.java for an example token fetch
        //      Use ClientService.createConstantTokenApiClient and fetch customers


        // 3: List of UpdateActions
        //      Compare the following three code snippets for updating a customer
        client
            .customers()
            .withKey("myCustomerKey")
            .post(CustomerUpdateBuilder.of()
                    .version(1L)
                    .actions(
                        CustomerSetFirstNameActionBuilder.of()
                            .firstName("his new first name ")
                            .build()
                    )
                    .build())
            .execute();

        client
            .customers()
            .withKey("myCustomerKey")
            .post(CustomerUpdateBuilder.of()
                    .version(1L)
                    .actions(
                        CustomerSetLastNameActionBuilder.of()
                                .lastName("his new last name")
                                .build()
                    )
                    .build())
            .execute();


        client
            .customers()
            .withKey("myCustomerKey")
            .post(CustomerUpdateBuilder.of()
                    .version(1L)
                    .actions(
                        CustomerSetFirstNameActionBuilder.of()
                                .firstName("his new first name ")
                                .build(),
                        CustomerSetLastNameActionBuilder.of()
                                .lastName("his new last name")
                                .build()
                    )
                    .build())
            .execute();


            // 4: X-Correlation_ID
            //      Decorate client
            //      Be careful!!
            //      Run GET Project and inspect x-correlation-id in the headers

        ProjectApiRoot correlationIdClient = ApiRootBuilder.of()
                .defaultClient(
                        ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .build(),
                        ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(),
                        ServiceRegion.GCP_EUROPE_WEST1.getApiUrl()
                )
                .withMiddleware((request, next) -> next.apply(request).whenComplete((response, throwable) -> {
                    if (throwable.getCause() instanceof ApiHttpException) {
                        logger.info(((ApiHttpException) throwable.getCause()).getHeaders().getFirst(ApiHttpHeaders.X_CORRELATION_ID));
                    } else {
                        logger.info(response.getHeaders().getFirst(ApiHttpHeaders.X_CORRELATION_ID));
                    }
                }))
                .addCorrelationIdProvider(() -> projectKey + "/" + UUID.randomUUID())
                .build(projectKey);



        // Or, per request

        logger.info("Get project information with pre-set correlation id: " +
                client
                    .get()
                    .withHeader(ApiHttpHeaders.X_CORRELATION_ID, "MyServer15" + UUID.randomUUID())
                    .execute()
                    .toCompletableFuture().get()
                    .getBody().getKey()
        );

        // 5
        //      Simulate failover, 5xx errors
        //      Nice test: Replace with
        //                  new RetryMiddleware(20, Arrays.asList(404, 500, 503))
        //                  and query for wrong customer, inspect then logging about the re-tries

        ProjectApiRoot retryClient = ApiRootBuilder.of()
                .defaultClient(
                       ClientCredentials.of()
                                        .withClientId(clientId)
                                        .withClientSecret(clientSecret)
                                        .build(),
                       ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(),
                       ServiceRegion.GCP_EUROPE_WEST1.getApiUrl()
                )
                .addConcurrentModificationMiddleware(3)
                .withRetryMiddleware(3, Arrays.asList(500, 503))
                .build(projectKey);
        logger.info("Get project information via retryClient " +
                retryClient
                        .get()
                        .execute()
                        .toCompletableFuture().get()
                        .getBody().getKey()
        );

        ProjectApiRoot concurrentClient = ApiRootBuilder.of()
                .defaultClient(
                        ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .build(),
                        ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(),
                        ServiceRegion.GCP_EUROPE_WEST1.getApiUrl()
                )
                .addConcurrentModificationMiddleware(3)
                .build(projectKey);
        logger.info("Update customer via concurrentClient " +
                concurrentClient
                        .customers()
                        .withKey("nd-customer")
                        .post(CustomerUpdateBuilder.of()
                                .version(1L)
                                .actions(CustomerSetLastNameActionBuilder.of()
                                        .lastName("dixit")
                                        .build())
                                .build())
                        .execute()
                        .toCompletableFuture().get()
                        .getBody().getLastName()
        );
        concurrentClient.close();
    }
}
