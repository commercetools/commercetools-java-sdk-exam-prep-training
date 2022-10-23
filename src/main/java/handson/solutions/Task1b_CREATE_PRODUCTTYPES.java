package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_type.ProductType;
import handson.solutions.impl.ApiPrefixHelper;
import handson.solutions.impl.ProductTypeService;
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
        AtomicInteger atomicInteger = new AtomicInteger(0);
        logger.info("I've read the following product types from conc: " +
                String.join(" ",
                        productTypeService_Concept
                                .getProductTypes()
                                .get()
                                .getBody().getResults()
                                .stream()
                                .map(productType -> atomicInteger.incrementAndGet() +  ":" + productType.getName() + " ")
                                .collect(Collectors.toList())
                )
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
        // Synching would have been easier, wouldn't it have been?
        //
        StringBuilder dockerRun = new StringBuilder();
        dockerRun.append("docker run ");
        dockerRun.append(" -e SOURCE_PROJECT_KEY=" + "projectKey-to-give");
        dockerRun.append(" -e SOURCE_CLIENT_ID=" + "clientId-to-give");
        dockerRun.append(" -e SOURCE_CLIENT_SECRET=" + "clientSecret-to-give");
        dockerRun.append(" -e SOURCE_AUTH_URL=https://auth.europe-west1.gcp.commercetools.com");
        dockerRun.append(" -e SOURCE_API_URL=https://api.europe-west1.gcp.commercetools.com");
        dockerRun.append(" -e TARGET_PROJECT_KEY=" + "projectKey-to-give");
        dockerRun.append(" -e TARGET_CLIENT_ID=" + "clientId-to-give");
        dockerRun.append(" -e TARGET_CLIENT_SECRET=" + "clientSecret-to-give");
        dockerRun.append(" -e TARGET_AUTH_URL=https://auth.europe-west1.gcp.commercetools.com");
        dockerRun.append(" -e TARGET_API_URL=https://api.europe-west1.gcp.commercetools.com");
        dockerRun.append(" commercetools/commercetools-project-sync:5.1.2 -s productTypes");
        Process process = Runtime.getRuntime().exec(dockerRun.toString());
        process.waitFor();
        logger.info(process.exitValue() + " ");

    }
}
