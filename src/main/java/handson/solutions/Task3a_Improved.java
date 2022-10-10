package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ServiceRegion;
import com.commercetools.api.models.graph_ql.GraphQLRequestBuilder;
import com.commercetools.api.models.graph_ql.GraphQLResponse;
import handson.solutions.impl.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;

public class Task3a_Improved {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Processes and API Extensions
        // GraphQL

        Logger logger = LoggerFactory.getLogger(Task3a_Improved.class.getName());

        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        CustomerService customerService = new CustomerService(apiRoot_poc);
        CartService cartService = new CartService(apiRoot_poc);
        CustomObjectService customObjectService = new CustomObjectService(apiRoot_poc);
        OrderService orderService = new OrderService(apiRoot_poc);

       // TODO Step 1: Provide cart and customer
        //
        String cartId = "c662758e-1c83-454d-975e-ee38c752321d";
        String customerId = "a9c7b8ef-3ec9-4bec-8c55-17347d7268c8";
        String customObjectContainer = "BonusPointCalculationSchema";
        String customObjectKey = "allCartValues";
        String customerBonusFieldName = "bonus-points-custom-field";
        String projectKey = "training-exam-prep-exercise-01";
        String taxCategoryKey = "standard-tax";

        // TODO Step 1: Fetch customer bonus points, cart value, bonus points calculation schema
        //
        final GraphQLResponse graphqQLCartCustomerCustomObjectResponse = apiRoot_poc
                .graphql()
                .post(
                        GraphQLRequestBuilder.of()
                                .query(
                                        "{ "
                                                + " cart (id: \"" + cartId + "\" ) { totalPrice { currencyCode centAmount } } "
                                                + " customer (id : \"" + customerId + "\" ) { custom { customFieldsRaw { name value } } } "
                                                + " customObjects (container: \"" + customObjectContainer + "\" ) { results { key value } } "
                                                + " } "
                                )
                                .build()
                )
                .execute()
                .toCompletableFuture().get()
                .getBody();

        logger.info("GraphQl : " + graphqQLCartCustomerCustomObjectResponse.getData());

        // Fetch data
        String oldBonusPoints = StringUtils.substringBetween(
                                    graphqQLCartCustomerCustomObjectResponse.getData().toString(),
                                "bonus-points-custom-field, value=",
                                "}]}}, customObjects"
        );
        String totalCartValue = StringUtils.substringBetween(
                graphqQLCartCustomerCustomObjectResponse.getData().toString(),
                 "centAmount=",
                "}}, customer="
        );
        logger.info("Found bonus Points and Cart Value: " + oldBonusPoints + " " + totalCartValue);

        // Find factor, addon
        // Do some math ...
        int newBonusPointsStatus = 200;
        int earnedBonusPoints = 200;

        // TODO Step 2:
        // Create order, set custom line item on cart, change bonus points on customer
        //
        logger.info("Order Creation / Customer Bonus Points for customer : " +
            cartService.getCartById(cartId)
                .thenComposeAsync(cartApiHttpResponse ->
                                cartService.addCustomLineItem(
                                        cartApiHttpResponse,
                                        "bonus-points-earned",
                                        0l,
                                        "bonus-points-custom-line-item",
                                        taxCategoryKey))
                .thenComposeAsync(orderService::createOrder)
                .thenCombineAsync(customerService.getCustomerById(customerId),
                        (orderApiHttpResponse, customerApiHttpResponse) ->
                        customerService.addCustomFieldValue(
                                customerApiHttpResponse,
                                "bonus-points-custom-field",
                                earnedBonusPoints
                        ))
                .toCompletableFuture().get()
                            .toCompletableFuture().get()
                            .getBody().getKey()
        );

        // TODO Step 3
        // Check order (MC), Impex



        apiRoot_poc.close();
    }
}
