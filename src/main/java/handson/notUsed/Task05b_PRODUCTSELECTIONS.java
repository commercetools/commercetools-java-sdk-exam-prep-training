package handson.notUsed;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product_selection.AssignedProductReference;
import handson.solutions.impl.ApiPrefixHelper;
import handson.solutions.impl.ProductSelectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


/**
 *
 */
public class Task05b_PRODUCTSELECTIONS {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Logger logger = LoggerFactory.getLogger(Task05b_PRODUCTSELECTIONS.class.getName());
        final String globalApiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();
        final ProjectApiRoot client = createApiClient(globalApiClientPrefix);

        final ProductSelectionService productSelectionService = new ProductSelectionService(client);

        final String productSelectionKey = "mh-berlin-product-selection";

        // TODO: Create product selection and add a product to the product selection

//        ProductSelection productSelection = productSelectionService.createProductSelection(productSelectionKey,"MH Berlin Product Selection")
//                    .thenComposeAsync(productSelectionApiHttpResponse ->
//                            productSelectionService.addProductToProductSelection(productSelectionApiHttpResponse,"tulip-seed-product"))
//                .toCompletableFuture().get()
//                .getBody();
//        logger.info("Created product selection: " + productSelection.getId());
//
//
//
//        // TODO: Get a store and assign the product selection to the store
//

//        logger.info("Product selections assigned to the store: "+
//                productSelectionService.getStoreByKey("berlin-store")
//                        .thenCombineAsync(productSelectionService.getProductSelectionByKey(productSelectionKey),((storeApiHttpResponse, productSelectionApiHttpResponse) ->
//                                productSelectionService.addProductSelectionToStore(storeApiHttpResponse,productSelectionApiHttpResponse))
//                        )
//                        .thenComposeAsync(CompletableFuture::toCompletableFuture)
//                        .toCompletableFuture().get()
//                        .getBody().getProductSelections().size()
//        );
//
//
//        // TODO Get products in a product selection
//
        List<AssignedProductReference> assignedProductReferences =
                productSelectionService.getProductsInProductSelection(productSelectionKey)
                        .toCompletableFuture().get().getBody().getResults();

        assignedProductReferences.forEach(assignedProductReference -> logger.info(assignedProductReference.getProduct().getObj().getKey()));

    }
}
