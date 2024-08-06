package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_type.ProductType;
import handson.solutions.impl.ProductTypeService;
import static handson.solutions.impl.ClientService.createApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Task1b_CREATE_PRODUCTTYPES {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {


        Logger logger = LoggerFactory.getLogger("commercetools");

        final String productTypeKey = "flowers-product-type";

        final ProjectApiRoot apiRoot = createApiClient("ctp");
        ProductTypeService productTypeService = new ProductTypeService(apiRoot);

        final ProjectApiRoot apiRoot_src = createApiClient("happy-garden-src-project-read");
        ProductTypeService productTypeService_Source = new ProductTypeService(apiRoot_src);

        AtomicInteger atomicInteger = new AtomicInteger(0);
        logger.info("I've read the following product types from conc: " +
                String.join(" ",
                        productTypeService_Source
                                .getProductTypes()
                                .get()
                                .getBody().getResults()
                                .stream()
                                .map(productType -> atomicInteger.incrementAndGet() +  ":" + productType.getName() + " ")
                                .collect(Collectors.toList())
                )
        );

        // Use ProductTypeService to read any number of product types
        ProductType productType = productTypeService_Source
                .getProductTypeByKey(productTypeKey)
                .get()
                .getBody();

        // Replicate using the replicateProductType method in ProductTypeService
        logger.info("I've created the following product type in poc: " +
                productTypeService.replicateProductType(productType)
                    .get()
                    .getBody().getName()
        );


        apiRoot_src.close();
        apiRoot.close();


        /*// TODO
        // Other ideas?
        // https://github.com/commercetools/commercetools-project-sync
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
        */
    }
}
