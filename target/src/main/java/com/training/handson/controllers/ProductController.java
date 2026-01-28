package com.training.handson.controllers;

import com.commercetools.api.models.product.ProductProjection;
import com.commercetools.api.models.product_search.ProductPagedSearchResponse;
import com.training.handson.dto.SearchRequest;
import com.training.handson.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public CompletableFuture<ResponseEntity<ProductPagedSearchResponse>> getProducts(
            @ModelAttribute SearchRequest searchRequest) {
        return productService.getProducts(searchRequest).thenApply(ResponseConverter::convert);
    }

    @GetMapping("/key={key}")
    public CompletableFuture<ResponseEntity<ProductProjection>> getProductByKey(@PathVariable String key) {
        return productService.getProductByKey(key).thenApply(ResponseConverter::convert);
    }
}
