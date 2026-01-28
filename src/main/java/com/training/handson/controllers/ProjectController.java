package com.training.handson.controllers;

import com.commercetools.api.models.project.Project;
import com.commercetools.api.models.store.StorePagedQueryResponse;
import com.training.handson.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/project-settings/")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("stores")
    public CompletableFuture<ResponseEntity<StorePagedQueryResponse>> getStores() {
        return projectService.getStores().thenApply(ResponseConverter::convert);
    }

    @GetMapping("countries")
    public CompletableFuture<ResponseEntity<List<String>>> getCountries() {
        return projectService.getCountries()
            .thenApply(ResponseEntity::ok);
    }

}
