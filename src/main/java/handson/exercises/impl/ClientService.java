package handson.exercises.impl;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import com.commercetools.importapi.defaultconfig.ImportApiRootBuilder;
import io.vrap.rmf.base.client.AuthenticationToken;
import io.vrap.rmf.base.client.HttpClientSupplier;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import io.vrap.rmf.base.client.oauth2.ClientCredentialsTokenSupplier;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ClientService {

    public static ProjectApiRoot projectApiRoot;
    public static com.commercetools.importapi.client.ProjectApiRoot importApiRoot;

    /**
     * @throws IOException exception
     */
    public static ProjectApiRoot createApiClient(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));
        String clientId = prop.getProperty(prefix + "clientId");
        String clientSecret = prop.getProperty(prefix + "clientSecret");
        String projectKey = prop.getProperty(prefix + "projectKey");

        projectApiRoot = ApiRootBuilder.of()
                .defaultClient(
                        ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .build(),
                        ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(),
                        ServiceRegion.GCP_EUROPE_WEST1.getApiUrl()
                )
                .build(projectKey);

        return projectApiRoot;
    }


    public static String getProjectKey(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));

        return prop.getProperty(prefix + "projectKey");
    }

    public static String getClientId(final String prefix) throws IOException {
        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));

        return prop.getProperty(prefix + "clientId");
    }


    public static String getClientSecret(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));

        return prop.getProperty(prefix + "clientSecret");
    }



    /**
     * @return apiRoot
     * @throws IOException exception
     */
    public static com.commercetools.importapi.client.ProjectApiRoot createImportApiClient(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));
        String clientId = prop.getProperty(prefix + "clientId");
        String clientSecret = prop.getProperty(prefix + "clientSecret");
        String projectKey = prop.getProperty(prefix + "projectKey");

        importApiRoot = ImportApiRootBuilder.of().defaultClient(
                ClientCredentials.of()
                        .withClientId(clientId)
                        .withClientSecret(clientSecret)
                        .build(),
                com.commercetools.importapi.defaultconfig.ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(),
                com.commercetools.importapi.defaultconfig.ServiceRegion.GCP_EUROPE_WEST1.getApiUrl()
            )
            .build(projectKey);

        return importApiRoot;
    }



}
