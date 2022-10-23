package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.LocalizedStringBuilder;
import com.commercetools.api.models.type.CustomFieldNumberType;
import com.commercetools.api.models.type.FieldDefinitionBuilder;
import com.commercetools.api.models.type.ResourceTypeId;
import handson.solutions.impl.ApiPrefixHelper;
import handson.solutions.impl.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static handson.solutions.impl.ClientService.createApiClient;


public class Task2a_CUSTOM_TYPES {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Get Query
        // Custom Types

        Logger logger = LoggerFactory.getLogger(Task2a_CUSTOM_TYPES.class.getName());

        final ProjectApiRoot apiRoot_poc =
                createApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        ConfigurationService configurationService = new ConfigurationService(apiRoot_poc);

        // TODO Step 1: Use ConfigurationService.java to check if custom type exists for storing customers' bonus points
        logger.info("Custom types for customizing customers: " +
                configurationService.getCustomTypes()
                    .get()
                    .getBody().getResults()
                    .size()
        );



        final LocalizedString localizedNameForBonuspoints = LocalizedStringBuilder.of()
                .values(new HashMap<String, String>() {
                    {
                        put("DE", "Bonuspunkte für Kunden");
                        put("EN", "Bonus points on customers");
                    }
                })
                .build();

        // TODO Step 2: Create a custom type for  storing bonus points to customers using ConfigurationService
        // You can use above localizedString for all name fields
        logger.info("Custom type with a custom field for bonus points created : " +
                configurationService.createCustomType(
                            "customer-bonuspoint-extension",
                            localizedNameForBonuspoints,
                            ResourceTypeId.CUSTOMER,
                            Arrays.asList(FieldDefinitionBuilder.of()
                                                .name("bonus-points-custom-field")
                                                .required(false)
                                                .label(localizedNameForBonuspoints)
                                                .type(CustomFieldNumberType.of())
                                                .build()
                            )
                        )
                        .get()
                        .getBody().getKey()
        );

        // TODO Step 3: Provide a random number 1..100 in the Merchnat Center for your customer

        apiRoot_poc.close();
    }
}
