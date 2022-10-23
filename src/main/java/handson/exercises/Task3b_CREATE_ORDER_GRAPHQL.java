package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.customer.Customer;
import handson.exercises.impl.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;

public class Task3b_CREATE_ORDER_GRAPHQL {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Create an Order
        // GraphQL queries

        Logger logger = LoggerFactory.getLogger(Task4_SUBSCRIPTIONS.class.getName());

        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        CustomerService customerService = new CustomerService(apiRoot_poc);
        CartService cartService = new CartService(apiRoot_poc);
        CustomObjectService customObjectService = new CustomObjectService(apiRoot_poc);
        OrderService orderService = new OrderService(apiRoot_poc);

       // TODO Step 1: Provide cart id and customer key
        //
        String cartId = "";
        String customerKey = "customer-michael15";
        String customObjectContainer = "Schemas";
        String customObjectKey = "bonusPointsCalculationSchema";
        String customerBonusFieldName = "bonus-points-custom-field";
        String taxCategoryKey = "standard-tax-category";

        // TODO Step 1: Customer wants to create an order, get all the data to update their bonus points
        //
        final Cart cart = cartService.getCartById(cartId)
                .get()
                .getBody();
        final Customer customer = customerService.getCustomerByKey(customerKey)
                .get()
                .getBody();
        final CustomObject customObject = customObjectService.getCustomObject(customObjectContainer, customObjectKey)
                .get()
                .getBody();

        // Log custom object
        logger.info("Custom Object retrieved: " + customObject.getValue().toString());

        // TODO Now, improve the query with GraphQL

        // TODO Step 1: Fetch customer bonus points, cart value, bonus points calculation schema
        // Single GraphQL query to fetch all the information you need to place an order
        /*final GraphQLResponse graphqQLCartCustomerCustomObjectResponse = apiRoot_poc
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

        final JSONObject allCartValues = data.getJSONObject("customObjects").getJSONArray("results").getJSONObject(0).getJSONObject("value");

        final int oldBonusPoints = (int) data.getJSONObject("customer")
                .getJSONObject("custom")
                .getJSONArray("customFieldsRaw")
                .getJSONObject(0)
                .get("value");

        final int totolPrice = (int) data.getJSONObject("cart")
                .getJSONObject("totalPrice")
                .get("centAmount");

        logger.info("Found current bonus Points: "
                + oldBonusPoints
                + " and the Cart Value: "
                + totolPrice
        );

        // Find factor, addon
        // Do some maths to calculate the bonus points

        int earnedBonusPoints = calculateBonusPoints(totolPrice,allCartValues);
        logger.info("Earned bonus points: " + earnedBonusPoints);

        // TODO Step 2:
        // Add custom line item in the cart for bonus points
        // Create order, update bonus points on customer
        //
        logger.info("Order Creation / Customer Bonus Points for customer : " +
            ""
        );*/

        // TODO Step 3
        // Check order (MC), Impex

        apiRoot_poc.close();
    }

    private static int calculateBonusPoints(
            final int cartValue,
            final JSONObject allCartValues
        ) {
            Iterator<String> keys = allCartValues.keys();
            while (keys.hasNext()){
                String key = keys.next();
                JSONObject bonusPointCalculator = allCartValues.getJSONObject(key);
                if(cartValue >= Integer.parseInt(key) && cartValue <= bonusPointCalculator.getInt("maxCartValue")){
                    int factor = bonusPointCalculator.getInt("factor");
                    int addon = bonusPointCalculator.getInt("addon");
                    return  (cartValue/100) * factor + addon;
                }
            }
            return 0;
    }
}
