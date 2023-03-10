package handson.solutions.impl;

import com.commercetools.importapi.client.ProjectApiRoot;
import com.commercetools.importapi.models.importcontainers.ImportContainer;
import com.commercetools.importapi.models.importcontainers.ImportContainerDraftBuilder;
import com.commercetools.importapi.models.importrequests.ImportResponse;
import com.commercetools.importapi.models.importrequests.ProductDraftImportRequest;
import com.commercetools.importapi.models.importrequests.ProductDraftImportRequestBuilder;
import com.commercetools.importapi.models.importsummaries.ImportSummary;
import com.commercetools.importapi.models.productdrafts.ProductDraftImport;
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

            return apiRoot
                    .importContainers()
                    .post(
                            ImportContainerDraftBuilder.of()
                                   .key(containerKey)
                                   .build()
                    )
                    .execute();
        }
    public CompletableFuture<ApiHttpResponse<ImportResponse>> importProduct(final String containerKey, final ProductDraftImport productDraftImport) {

        return apiRoot
                .productDrafts()
                .importContainers()
                .withImportContainerKeyValue(containerKey)
                .post(
                        ProductDraftImportRequestBuilder.of()
                                .resources(productDraftImport)
                                .build()
                )
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<ImportSummary>> getImportSummaryByContainer(final String containerKey) {

        return apiRoot
                .importContainers()
                .withImportContainerKeyValue(containerKey)
                .importSummaries()
                .get()
                .execute();
    }
}
