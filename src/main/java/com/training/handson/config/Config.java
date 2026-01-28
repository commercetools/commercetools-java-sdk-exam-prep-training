package com.training.handson.config;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.ApiHttpMethod;
import io.vrap.rmf.base.client.http.ErrorMiddleware;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.UUID;

import static io.vrap.rmf.base.client.http.HttpStatusCode.*;

@Configuration
public class Config {

    @Value("${ctp.clientId}")
    private String clientId;

    @Value("${ctp.clientSecret}")
    private String clientSecret;

    @Value("${ctp.scopes}")
    private String scopes;

    @Value("${ctp.projectKey}")
    private String projectKey;


    @Bean
    public ProjectApiRoot projectApiRoot() {
        return ApiRootBuilder.of()
                .defaultClient(
                        ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .withScopes(scopes)
                                .build(),
                        ServiceRegion.GCP_EUROPE_WEST1
                )
                .withPolicies(policyBuilder ->
                        policyBuilder.withRetry(retryPolicyBuilder ->
                                retryPolicyBuilder.maxRetries(3).statusCodes(Arrays.asList(BAD_GATEWAY_502, SERVICE_UNAVAILABLE_503, GATEWAY_TIMEOUT_504))))
                .withErrorMiddleware(ErrorMiddleware.ExceptionMode.UNWRAP_COMPLETION_EXCEPTION)
                .addNotFoundExceptionMiddleware(apiHttpRequest -> apiHttpRequest.getMethod() == ApiHttpMethod.GET
                        || apiHttpRequest.getMethod() == ApiHttpMethod.DELETE)
                .addConcurrentModificationMiddleware(3)
                .addCorrelationIdProvider(() -> projectKey + "/" + UUID.randomUUID())
                .build(projectKey);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
