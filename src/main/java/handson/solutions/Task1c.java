package handson.solutions;

import com.commercetools.importapi.client.ProjectApiRoot;
import com.commercetools.importapi.models.importsummaries.OperationStates;
import handson.solutions.impl.ApiPrefixHelper;
import handson.solutions.impl.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createImportApiClient;


public class Task1c {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Import API: Sinks
        // Import API: Logging states

        Logger logger = LoggerFactory.getLogger(Task1c.class.getName());

        // TODO Step 1: Provide your container key
        //
        final String containerKey = "XX-exam-prep-product-date-wrong";

        // Create an admin import api client for your project
        // Use ClientService.class
        final ProjectApiRoot apiRoot_poc_import =
                createImportApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        ImportService importService = new ImportService(apiRoot_poc_import);

        logger.info("I've created the following sink for poc: " +
                importService.createImportContainer(containerKey)
                    .toCompletableFuture().get()
                    .getBody().getKey()
        );

        // Prepare for logging
        //
        logger.info("Total containers in our project: {}",
                apiRoot_poc_import
                        .importContainers()
                        .get()
                        .execute()
                        .toCompletableFuture().get()
                        .getBody().getTotal()
        );
        OperationStates states = apiRoot_poc_import
                .importContainers()
                .withImportContainerKeyValue(containerKey)
                .importSummaries()
                .get()
                .execute()
                .toCompletableFuture().get()
                .getBody().getStates();

        logger.info("Processing: {} Imported: {} Unresolved: {} ",
                states.getProcessing(),
                states.getImported(),
                states.getUnresolved()
        );

        apiRoot_poc_import.close();

    }
}
