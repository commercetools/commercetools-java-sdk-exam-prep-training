package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.project.Project;
import com.commercetools.api.models.store.StorePagedQueryResponse;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProjectService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<StorePagedQueryResponse>> getStores() {
        return
                apiRoot
                        .stores()
                        .get()
                        .execute();
    }

    public CompletableFuture<List<String>> getCountries() {
        return apiRoot
            .get()
            .execute()
                .thenApply(ApiHttpResponse::getBody)
                .thenApply(Project::getCountries);
    }
}
