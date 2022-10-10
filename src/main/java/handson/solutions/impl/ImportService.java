package handson.solutions.impl;

import com.commercetools.importapi.client.ProjectApiRoot;
import com.commercetools.importapi.models.importcontainers.ImportContainer;
import com.commercetools.importapi.models.importcontainers.ImportContainerDraftBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class ImportService {

    final ProjectApiRoot apiRoot;

    public ImportService(final ProjectApiRoot client) {
        this.apiRoot = client;
    }

    public CompletableFuture<ApiHttpResponse<ImportContainer>> createImportContainer(final String containerKey) {

            return
                apiRoot
                        .importContainers()
                        .post(
                                ImportContainerDraftBuilder.of()
                                       .key(containerKey)
                                       .build()
                        )
                        .execute();
        }

}
