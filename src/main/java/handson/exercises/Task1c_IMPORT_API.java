package handson.exercises;

import com.commercetools.importapi.client.ProjectApiRoot;
import com.commercetools.importapi.models.importsummaries.OperationStates;
import handson.exercises.impl.ImportService;
import static handson.exercises.impl.ClientService.createImportApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;



public class Task1c_IMPORT_API {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Import API: Import Containers
        // Import API: Logging states

        Logger logger = LoggerFactory.getLogger("commercetools");

        // TODO Step 1: Provide your container key
        //
        final String containerKey = "mh-product-data-container";

        // Create an admin import api client for your project
        // Use ClientService.class

        final ProjectApiRoot apiRoot = createImportApiClient("import");
        ImportService importService = new ImportService(apiRoot);

        logger.info("I've created the following Import Container : " +
                importService.createImportContainer(containerKey)
                    .get()
                    .getBody().getKey()
        );

        // Prepare for logging
        //
        logger.info("Total containers in our project: {}",
                apiRoot
                        .importContainers()
                        .get()
                        .execute()
                        .get()
                        .getBody().getTotal()
        );
        OperationStates states = importService.getImportSummaryByContainer(containerKey)
                .get().getBody().getStates();

        logger.info("Processing: {} Imported: {} Unresolved: {} ",
                states.getProcessing(),
                states.getImported(),
                states.getUnresolved()
        );

        apiRoot.close();

    }
}
