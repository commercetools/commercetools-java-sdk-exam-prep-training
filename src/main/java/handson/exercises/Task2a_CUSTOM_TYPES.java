package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.LocalizedStringBuilder;
import static handson.exercises.impl.ClientService.createApiClient;
import handson.exercises.impl.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class Task2a_CUSTOM_TYPES {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Get Query
        // Custom Types

        Logger logger = LoggerFactory.getLogger("commercetools");

        final ProjectApiRoot apiRoot = createApiClient("poc");
        ConfigurationService configurationService = new ConfigurationService(apiRoot);

        // TODO Step 1: Use ConfigurationService.java to check if a custom type exists for storing customers' bonus points
        logger.info("Custom types for customizing customers: " +
                configurationService.getCustomTypes()
                    .get()
                    .getBody().getResults()
                    .size()
        );



        final LocalizedString localizedNameForBonuspoints = LocalizedStringBuilder.of()
                .values(new HashMap<String, String>() {
                    {
                        put("de", "Bonuspunkte");
                        put("en", "Bonus points");
                    }
                })
                .build();

        // TODO Step 2: Create a custom type for  storing bonus points to customers using ConfigurationService
        // You can use above localizedString for all name fields
        logger.info("Custom type with a custom field for bonus points created : " +
                ""
        );

        // TODO Step 3: In the Merchnat Center, provide a random number 1..100 as bonus for your customer

        apiRoot.close();
    }
}
