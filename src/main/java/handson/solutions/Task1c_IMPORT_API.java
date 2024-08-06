package handson.solutions;

import com.commercetools.importapi.client.ProjectApiRoot;
import com.commercetools.importapi.models.importsummaries.OperationStates;
import handson.solutions.impl.ImportService;
import static handson.solutions.impl.ClientService.createImportApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;




public class Task1c_IMPORT_API {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Logger logger = LoggerFactory.getLogger("commercetools");

        final String containerKey = "nd-product-data-container";

        final ProjectApiRoot apiRoot = createImportApiClient("import");
        ImportService importService = new ImportService(apiRoot);

        //Create a new Import Container
        logger.info("I've created the following Import Container for poc: " +
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
