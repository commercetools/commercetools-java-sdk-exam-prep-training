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


    public static String getStoreKey(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));

        return prop.getProperty(prefix + "storeKey");
    }


    public static String getCustomerEmail(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));

        return prop.getProperty(prefix + "customerEmail");
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


    public static ProjectApiRoot createMeTokenApiClient(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));
        String projectKey = prop.getProperty(prefix + "projectKey");
        String customerEmail = prop.getProperty(prefix + "customerEmail");
        String customerPassword = prop.getProperty(prefix + "customerPassword");
        String clientId = prop.getProperty(prefix + "clientId");
        String clientSecret = prop.getProperty(prefix + "clientSecret");

        return ApiRootBuilder.of().defaultClient(
                     ServiceRegion.GCP_EUROPE_WEST1.getApiUrl()
                )
                .withGlobalCustomerPasswordFlow(
                        ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .build(),
                        customerEmail,
                        customerPassword,
            ServiceRegion.GCP_EUROPE_WEST1.getAuthUrl() + "/oauth/" + projectKey + "/customers/token"
                )
                .build(projectKey);
    }

    public static ProjectApiRoot createStoreMeApiClient(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));
        String projectKey = prop.getProperty(prefix + "projectKey");
        String storeKey = prop.getProperty(prefix + "storeKey");
        String storeCustomerEmail = prop.getProperty(prefix + "customerEmail");
        String storeCustomerPassword = prop.getProperty(prefix + "customerPassword");
        String clientId = prop.getProperty(prefix + "clientId");
        String clientSecret = prop.getProperty(prefix + "clientSecret");

        return ApiRootBuilder.of().defaultClient(ServiceRegion.GCP_EUROPE_WEST1.getApiUrl())
                .withGlobalCustomerPasswordFlow(
                        ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .build(),
                        storeCustomerEmail,
                        storeCustomerPassword,
            ServiceRegion.GCP_EUROPE_WEST1.getAuthUrl() + "/oauth/" + projectKey + "/in-store/key=" + storeKey + "/customers/token"
                )
                .build(projectKey);
    }


    public static AuthenticationToken getTokenForClientCredentialsFlow(final String prefix) throws IOException {

        final Properties prop = new Properties();
        prop.load(ClientService.class.getResourceAsStream("/dev.properties"));
        String clientId = prop.getProperty(prefix + "clientId");
        String clientSecret = prop.getProperty(prefix + "clientSecret");
        AuthenticationToken token = null;
        try (final ClientCredentialsTokenSupplier clientCredentialsTokenSupplier = new ClientCredentialsTokenSupplier(
                clientId,
                clientSecret,
                null,
                ServiceRegion.GCP_EUROPE_WEST1.getOAuthTokenUrl(),
                HttpClientSupplier.of().get()
        )) {
            token = clientCredentialsTokenSupplier.getToken().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return token;
    }

}
