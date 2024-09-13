package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_type.ProductType;
import handson.exercises.impl.ProductTypeService;
import static handson.exercises.impl.ClientService.createApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Task1b_CREATE_PRODUCTTYPES {


    // TODO
    // convert Arrays as list to stream and expect any number of attributes
    // read any number of product types
    // ========================


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Get-PagedResponse
        // Products require ProductTypes !!
        // Post complicated drafts
        // Project Sync Tool

        // TODO Step 1: Provide credentials in dev.properties for conc-client
        // TODO Step 2: Provide prefix in APIHelper for conc-client

        Logger logger = LoggerFactory.getLogger("commercetools");

        final String productTypeKey = "flowers-product-type";


        final ProjectApiRoot apiRoot = createApiClient("poc");
        ProductTypeService productTypeService = new ProductTypeService(apiRoot);

        final ProjectApiRoot apiRoot_src = createApiClient("happy-garden-src-project-read");
        ProductTypeService productTypeService_source = new ProductTypeService(apiRoot_src);


        // TODO Step 3: Use ProductTypeService.class to read any number of product types

        logger.info("I've read the following product types from source project: " +
                ""
        );

        ProductType productType = productTypeService_source
                .getProductTypeByKey(productTypeKey)
                .get()
                .getBody();

        // TODO Step 4: Write the transferProductType  in ProductTypeService

        logger.info("I've created the following product type in the project: " +
                productTypeService.replicateProductType(productType)
                    .get()
                    .getBody().getName()
        );


        apiRoot.close();
        apiRoot_src.close();

    }
}
