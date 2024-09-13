package handson.solutions;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.LocalizedStringBuilder;
import com.commercetools.api.models.type.CustomFieldNumberType;
import com.commercetools.api.models.type.FieldDefinitionBuilder;
import com.commercetools.api.models.type.ResourceTypeId;
import handson.solutions.impl.ConfigurationService;
import static handson.solutions.impl.ClientService.createApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class Task2a_CUSTOM_TYPES {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        Logger logger = LoggerFactory.getLogger("commercetools");

        final ProjectApiRoot apiRoot = createApiClient("ctp");
        ConfigurationService configurationService = new ConfigurationService(apiRoot);

        // Use ConfigurationService.java to check if custom type exists for storing customers' bonus points
        logger.info("Custom types for customizing customers: " +
                configurationService.getCustomTypes()
                    .get()
                    .getBody().getResults()
                    .size()
        );

        final LocalizedString localizedNameForBonuspoints = LocalizedStringBuilder.of()
                .values(new HashMap<String, String>() {
                    {
                        put("en", "Bonus points");
                        put("de", "Bonuspunkte");
                    }
                })
                .build();

        // Create a custom type for  storing bonus points for customers using ConfigurationService
        // You can use above localizedString for all name fields
        logger.info("Custom type with a custom field for bonus points created : " +
                configurationService.createCustomType(
                            "bonus-point-custom-type",
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

        // In the Merchnat Center, provide a random number 1..100 as bonus for your customer

        apiRoot.close();
    }
}
