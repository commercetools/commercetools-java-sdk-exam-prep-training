package handson.notUsed;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.CartDraftBuilder;
import com.commercetools.api.models.me.MyCartDraftBuilder;
import handson.solutions.impl.ApiPrefixHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.*;


/**
 *
 */
public class Task05a_INSTORE_ME {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Logger logger = LoggerFactory.getLogger(Task05a_INSTORE_ME.class.getName());

        // TODO: Create in-store cart with global API client
        //  Provide an API client with global permissions
        //  Provide a customer who is restricted to a store
        //  Note: A global cart creation should fail but an in-store cart should world
        //

        final String globalApiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();
        final ProjectApiRoot client = createApiClient(globalApiClientPrefix);

        logger.info("Created in-store cart with a global api client: " +
                client
                .inStore("berlin-store")
                .carts()
                .post(
                        CartDraftBuilder.of()
                                .deleteDaysAfterLastModification(90L)
                                .customerId("7014afc5-210f-4be9-955f-c3707142f325")
                                .currency("EUR")
                                .customerEmail("nagesh@dtest.com")
                                .build()
                )
                .execute()
                .toCompletableFuture().get()
                .getBody().getId()
        );
        client.close();



        // TODO: Create in-store Cart with in-store API client
        //  Update the ApiPrefixHelper with the prefix for Store API Client
        //  Provide an API client with scope limited to a store
        //  Provide a customer with only store permissions
        //  Try creating a global cart with a global customer and check the error message


        final String storeApiClientPrefix = ApiPrefixHelper.API_STORE_CLIENT_PREFIX.getPrefix();
        final String storeKey = getStoreKey(storeApiClientPrefix);
        final ProjectApiRoot storeClient = createApiClient(storeApiClientPrefix);


        logger.info("Created in-store cart with a store api client: "+
                storeClient
                        .inStore(storeKey)
                        .carts()
                        .post(
                                CartDraftBuilder.of()
                                        .deleteDaysAfterLastModification(90L)
                                        .customerId("82cf41b6-3030-4fb1-86ac-d8be40d34878")
                                        .currency("EUR")
                                        .customerEmail("nagesh@test.com")
                                        .build()
                        )
                        .execute()
                        .toCompletableFuture().get()
                        .getBody().getId()
        );
        storeClient.close();

        // TODO
        //  Visit impex to verify that the carts are holding the same information
        //


        // TODO: Create a cart via /me endpoint
        //  Provide API client with SPA for customer with global permissions
        //  Update the ApiPrefixHelper with the prefix for Me(SPA) API Client
        //  You can also create in-store customer-bound cart
        //  Visit impex to inspect the carts created

        final String meApiClientPrefix = ApiPrefixHelper.API_ME_CLIENT_PREFIX.getPrefix();
        final ProjectApiRoot meClient = createMeTokenApiClient(meApiClientPrefix);
        final String customerEmail = getCustomerEmail(meApiClientPrefix);

        logger.info("Get cart for customer via me endpoint: " +
                meClient
                        .me()
                        .carts()
                        .post(
                                MyCartDraftBuilder.of()
                                                  .currency("EUR")
                                                  .deleteDaysAfterLastModification(90L)
                                                  .customerEmail(customerEmail)
                                                  .build()
                        )
                        .execute()
                        .exceptionally(throwable -> {
                            logger.info(throwable.getLocalizedMessage());
                            return null;
                        })
                        .toCompletableFuture().get()
                        .getBody().getId()
        );
        meClient.close();

        // TODO: Create in-store customer-bound Cart with in-store-me API client
        //  Update the ApiPrefixHelper with the prefix for Me(SPA) API Client
        //  Provide in-store-me API client with scope for a store and me endpoint
        //  Try creating a global cart without me and check the error message
        //  Visit impex to inspect the carts created

        final String storeMeApiClientPrefix = ApiPrefixHelper.API_STORE_ME_CLIENT_PREFIX.getPrefix();
        final ProjectApiRoot meStoreClient = createStoreMeApiClient(storeMeApiClientPrefix);
        final String meStoreKey = getStoreKey(storeMeApiClientPrefix);
        final String storeCustomerEmail = getCustomerEmail(storeMeApiClientPrefix);

        logger.info("Created in-store cart with a store api client: "+
                meStoreClient
                        .inStore(meStoreKey)
                        .me()
                        .carts()
                        .post(
                                MyCartDraftBuilder.of()
                                        .deleteDaysAfterLastModification(90L)
                                        .currency("EUR")
                                        .customerEmail(storeCustomerEmail)
                                        .build()
                        )
                        .execute()
                        .exceptionally(throwable -> {
                            logger.info(throwable.getLocalizedMessage());
                            return null;
                        })
                        .toCompletableFuture().get()
                        .getBody().getId()
        );
        meStoreClient.close();
    }
}
