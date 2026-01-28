package com.training.handson.controllers;

import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.type.Type;
import com.training.handson.dto.CustomObjectRequest;
import com.training.handson.services.ExtensionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/extensions/")
public class ExtensionsController {

    private final ExtensionsService extensionsService;

    public ExtensionsController(ExtensionsService extensionsService) {
        this.extensionsService = extensionsService;
    }

    @PostMapping("types")
    public CompletableFuture<ResponseEntity<Type>> createType() {

        return extensionsService.createType().thenApply(ResponseConverter::convert);
    }

    @PostMapping("custom-objects")
    public CompletableFuture<ResponseEntity<CustomObject>> createCustomObject(@RequestBody CustomObjectRequest customObjectRequest) {

        return extensionsService.createCustomObject(customObjectRequest).thenApply(ResponseConverter::convert);
    }

    @GetMapping("custom-objects")
    public CompletableFuture<ResponseEntity<CustomObject>> getCustomObject() {

        return extensionsService.getCustomObjectWithContainerAndKey().thenApply(ResponseConverter::convert);
    }
}

