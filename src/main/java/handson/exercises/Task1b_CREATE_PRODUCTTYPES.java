package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_type.ProductType;
import handson.exercises.impl.ApiPrefixHelper;
import handson.exercises.impl.ProductTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static handson.solutions.impl.ClientService.createApiClient;


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
        Logger logger = LoggerFactory.getLogger(Task1b_CREATE_PRODUCTTYPES.class.getName());
        final ProjectApiRoot apiRoot_conc =
                    createApiClient(
                        ApiPrefixHelper.API_CONC_CLIENT_PREFIX.getPrefix()
                    );
        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        ProductTypeService productTypeService_Concept = new ProductTypeService(apiRoot_conc);
        ProductTypeService productTypeService_Poc = new ProductTypeService(apiRoot_poc);


        // TODO Step 3: Use ProductTypeService.class to read any number of product types

        logger.info("I've read the following product types from conc: " +
                ""
        );

        ProductType myRoses = productTypeService_Concept
                .getProductTypes()
                .get()
                .getBody().getResults().get(0);

        // TODO Step 4: Write the transferProductType-method in ProductTypeService.class
        // Assume 1 productType, 1 attribute
        logger.info("I've created the following product type in poc: " +
                productTypeService_Poc.transferProductType(myRoses)
                    .get()
                    .getBody().getName()
        );


        apiRoot_conc.close();
        apiRoot_poc.close();


        // TODO
        // Other ideas?
        //

    }
}
