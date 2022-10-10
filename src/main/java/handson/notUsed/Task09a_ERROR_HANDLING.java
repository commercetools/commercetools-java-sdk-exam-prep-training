package handson.notUsed;

import com.commercetools.api.client.ProjectApiRoot;
import handson.solutions.impl.ApiPrefixHelper;
import handson.solutions.impl.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


public class Task09a_ERROR_HANDLING {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        final String apiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();

        final ProjectApiRoot client = createApiClient(apiClientPrefix);
        Logger logger = LoggerFactory.getLogger(Task09a_ERROR_HANDLING.class.getName());

        CustomerService customerService = new CustomerService(client);

        // TODO:
        //  Provide a WRONG or CORRECT customer key
        //
        final String customerKeyMayOrMayNotExist = "customer-michele-WRONG-KEY";

        // TODO: Handle 4XX errors, exceptions
        //  Use CompletionStage

        /*
        //
        logger.info("Customer fetch: " +
                customerService
                        .getCustomerByKey(customerKeyMayOrMayNotExist)
                        .thenApply(ApiHttpResponse::getBody) // unpack response body
                        .exceptionally(throwable -> {
                            logger.info("Customer " + customerKeyMayOrMayNotExist + " does not exist.");
                            // handle it
                            return
                                    CustomerBuilder.of()
                                            .email("anonymous@example.org")
                                            .build();                               // e.g. return anon customer
                        })
                        .toCompletableFuture().get().getEmail()
        );


        // TODO: Handle 4XX errors, exceptions
        //  Use Optionals, Either (Java 9+)
        //
        Optional<Customer> optionalCustomer = Optional.ofNullable(
                customerService
                        .getCustomerByKey("customer-michele-WRONG-KEY")
                        .thenApply(ApiHttpResponse::getBody)
                        .exceptionally(throwable -> null)
                        .toCompletableFuture().get()
        );

        if (!optionalCustomer.isPresent()) {
            logger.info("Customer " + customerKeyMayOrMayNotExist + " does not exist.");
            // handle it, return anon customer, etc.
        }

        optionalCustomer.ifPresent(customer -> {
            logger.info("Customer: " + customerKeyMayOrMayNotExist + "exists.");
            try {
                customerService.createEmailVerificationToken(customer, 5)
                        .thenComposeAsync(customerTokenApiHttpResponse -> customerService.verifyEmail(
                                customerTokenApiHttpResponse.getBody()
                        ))
                        .toCompletableFuture().get();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });


         */
    }
}
