package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.graph_ql.GraphQLRequestBuilder;
import com.commercetools.api.models.graph_ql.GraphQLResponse;
import handson.exercises.impl.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class Task3b_CREATE_ORDER_GRAPHQL {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Create an Order
        // GraphQL queries

        Logger logger = LoggerFactory.getLogger("commercetools");

        final ProjectApiRoot apiRoot = ClientService.createApiClient("ctp");
        CustomerService customerService = new CustomerService(apiRoot);
        CartService cartService = new CartService(apiRoot);
        ConfigurationService configurationService = new ConfigurationService(apiRoot);
        OrderService orderService = new OrderService(apiRoot);

       // TODO Step 1: Provide cart id and customer key
        //
        String cartId = "";
        String customerKey = "customer-michael";
        String customObjectContainer = "Schemas";
        String customObjectKey = "bonus-points-calculation-schema";
        String customerBonusFieldName = "bonus-points-custom-field";
        String taxCategoryKey = "standard-tax";

        // TODO Step 1: Customer wants to create an order, get all the data to update their bonus points
        //

        // Single GraphQL query to fetch all the information you need to place an order
        final GraphQLResponse graphqQLCartCustomerCustomObjectResponse = apiRoot
                .graphql()
                .post(
                        GraphQLRequestBuilder.of()
                                .query(
                                        "{ "
                                                + " cart (id: \"" + cartId + "\" ) { totalPrice { currencyCode centAmount } } "
                                                + " customer (key : \"" + customerKey + "\" ) { custom { customFieldsRaw { name value } } } "
                                                + " customObjects (container: \"" + customObjectContainer + "\" ) { results { key value } } "
                                                + " } "
                                )
                                .build()
                )
                .execute()
                .get()
                .getBody();
        final JSONObject data = new JSONObject(graphqQLCartCustomerCustomObjectResponse).getJSONObject("data");

        final JSONObject bonusSchema = data.getJSONObject("customObjects").getJSONArray("results").getJSONObject(0).getJSONObject("value");

        final int bonusPoints = (int) data.getJSONObject("customer")
                .getJSONObject("custom")
                .getJSONArray("customFieldsRaw")
                .getJSONObject(0)
                .get("value");

        final int totolPrice = (int) data.getJSONObject("cart")
                .getJSONObject("totalPrice")
                .get("centAmount");

        logger.info("Found current bonus Points: "
                + bonusPoints
                + " and the Cart Value: "
                + totolPrice / 100
        );

        // Find factor, addon
        // Do some maths to calculate the bonus points

        int newBonusPoints = calculateBonusPoints(totolPrice, bonusSchema);
        logger.info("Earned bonus points: " + newBonusPoints);

        // TODO Step 2:
        // Add custom line item in the cart for bonus points
        // Create order, update bonus points on customer
        //
        logger.info("Order Creation / Customer Bonus Points for customer : " +
                        ""
        );
        // TODO Step 3
        // Check order (MC), Impex

        apiRoot.close();
    }

    private static int calculateBonusPoints(
            final int cartValue,
            final JSONObject bonusSchema
        ) {
            Iterator<String> keys = bonusSchema.keys();
            while (keys.hasNext()){
                String key = keys.next();
                JSONObject bonusPointCalculator = bonusSchema.getJSONObject(key);
                if(cartValue >= Integer.parseInt(key) && cartValue <= bonusPointCalculator.getInt("maxCartValue")){
                    int factor = bonusPointCalculator.getInt("factor");
                    int addon = bonusPointCalculator.getInt("addon");
                    return  (cartValue/100) * factor + addon;
                }
            }
            return 0;
    }
}
