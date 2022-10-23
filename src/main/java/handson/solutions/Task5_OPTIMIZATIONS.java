package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.order.OrderPagedQueryResponse;
import handson.solutions.impl.ApiPrefixHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;
import static handson.solutions.impl.ClientService.projectApiRoot;


public class Task5_OPTIMIZATIONS {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Bulk Download via continuations

        Logger logger = LoggerFactory.getLogger(Task5_OPTIMIZATIONS.class.getName());

        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );

        // TODO Step 1: Provide date for orders to be downloaded
        //
        String orderDate = "2022-10-10";

        // TODO Step 2: Fetch first order
        //
         // Pagination is down to max 10.000
        final int PAGE_SIZE = 1;
        boolean lastPage = false;

        String lastId = projectApiRoot
                .orders()
                .get()
                .addWhere("createdAt > \"" + orderDate + "\"")
                .withSort("id asc")
                .withLimit(1)
                .execute()
                .toCompletableFuture().get()
                .getBody().getResults().get(0).getId();
        lastId = lastId.substring(0,lastId.length() -1) + "0"; // Starting last id less than the first one

        logger.info("First order: " + lastId);

        // TODO Step 3: Inspect code
        // Correct code, it overruns the orderDate
        //
        while(!lastPage) {
           OrderPagedQueryResponse orderPagedQueryResponse =
                    apiRoot_poc
                            .orders()
                            .get()

                            // Important, internally we use id > $lastId, it will not work without this line
                            .withSort("id asc")

                            .withWhere("id > :lastId")
                            .withPredicateVar("lastId", lastId)

                            // Limit the size per page
                            .withLimit(PAGE_SIZE)

                            // always use this
                            .withWithTotal(false)

                            .execute()
                            .toCompletableFuture().get()
                            .getBody();

            // Print results
            int size = orderPagedQueryResponse.getResults().size();

            if ( size != 0) {
                logger.info("////////////////////////////////");
                logger.info("Found orders: " + size);
                orderPagedQueryResponse.getResults().forEach(
                        order -> logger.info("Product: " + order.getId())
                );
                lastId = orderPagedQueryResponse.getResults().get(size - 1).getId();
            }
            lastPage = !(size == PAGE_SIZE);
        }

        apiRoot_poc.close();
    }
}
