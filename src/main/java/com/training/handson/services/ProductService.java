package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.ProductProjection;
import com.commercetools.api.models.product_search.*;
import com.commercetools.api.models.search.*;
import com.training.handson.dto.SearchRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class ProductService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<ProductProjection>> getProductByKey(String productKey) {
        return apiRoot
                .productProjections()
                .withKey(productKey)
                .get()
                .execute();
    }

        public CompletableFuture<ApiHttpResponse<ProductPagedSearchResponse>> getProducts(SearchRequest searchRequest) {
            ProductSearchRequestBuilder builder = ProductSearchRequestBuilder.of()
                    .withSort(buildSort(searchRequest))
                    .productProjectionParameters(productSearchProjectionParamsBuilder -> productSearchProjectionParamsBuilder
                            .priceCurrency(searchRequest.getCurrency())
                            .priceCountry(searchRequest.getCountry())
                            )
                    .markMatchingVariants(true);


            if (searchRequest.isFacets()){
                builder.facets(createFacets(searchRequest));
            }

            boolean hasKeyword = StringUtils.isNotEmpty(searchRequest.getKeyword());
            boolean hasStoreKey = StringUtils.isNotEmpty(searchRequest.getStoreKey());

            if (hasKeyword || hasStoreKey) {
                if (hasKeyword && hasStoreKey) {
                    builder.query(createSearchQuery(searchRequest))
                            .productProjectionParameters(createProductProjectionParams(searchRequest));
                } else if (hasKeyword) {
                    builder.query(createFullTextQuery(searchRequest));
                } else {
                    final String storeId = getStoreId(searchRequest.getStoreKey());
                    builder.query(createStoreQuery(storeId))
                        .productProjectionParameters(createProductProjectionParams(searchRequest));
                }
            }

        return apiRoot
                .products()
                .search()
                .post(builder.build())
                .execute();
    }

    private Function<SearchSortingBuilder, SearchSortingBuilder> buildSort(SearchRequest request) {
        return ssb -> ssb
                .field("variants.prices.centAmount")
                .mode(SearchSortMode.MIN)
                .order(SearchSortOrder.ASC)
                .filter(
                        SearchAndExpressionBuilder.of().and(List.of(
                                SearchExactExpressionBuilder.of().exact(
                                        SearchExactValueBuilder.of()
                                                .field("variants.prices.currencyCode")
                                                .value(request.getCurrency())
                                                .build()
                                ).build(),
                                SearchExactExpressionBuilder.of().exact(
                                        SearchExactValueBuilder.of()
                                                .field("variants.prices.country")
                                                .value(request.getCountry())
                                                .build()
                                ).build()
                        )).build()
                );
    }

    private List<ProductSearchFacetExpression> createFacets(SearchRequest searchRequest){
        return Arrays.asList(ProductSearchFacetDistinctExpressionBuilder.of()
                        .distinct(
                                productSearchFacetDistinctValueBuilder -> productSearchFacetDistinctValueBuilder
                                        .name("Color")
                                        .field("variants.attributes.search-color.label")
                                        .fieldType(SearchFieldType.LENUM)
                                        .language(searchRequest.getLocale())
                                        .level(ProductSearchFacetCountLevelEnum.VARIANTS)
                                        .scope(ProductSearchFacetScopeEnum.ALL)
                        )
                        .build(),
                ProductSearchFacetDistinctExpressionBuilder.of()
                        .distinct(
                                productSearchFacetDistinctValueBuilder -> productSearchFacetDistinctValueBuilder
                                        .name("Finish")
                                        .field("variants.attributes.search-finish.label")
                                        .fieldType(SearchFieldType.LENUM)
                                        .language(searchRequest.getLocale())
                                        .level(ProductSearchFacetCountLevelEnum.VARIANTS)
                                        .scope(ProductSearchFacetScopeEnum.ALL)
                        )
                        .build()
        );
    }

    private String getStoreId(String storeKey) {
        return apiRoot.stores().withKey(storeKey).get().executeBlocking().getBody().getId();
    }

    private SearchQuery createSearchQuery(SearchRequest searchRequest) {
        final String storeId = getStoreId(searchRequest.getStoreKey());
        return SearchAndExpressionBuilder.of()
                .and(Arrays.asList(
                        createFullTextQuery(searchRequest),
                        createStoreQuery(storeId)
                ))
                .build();
    }

    private SearchQuery createFullTextQuery(SearchRequest searchRequest) {
        return SearchFullTextExpressionBuilder.of()
                .fullText(searchFullTextValueBuilder -> searchFullTextValueBuilder
                        .field("name")
                        .value(searchRequest.getKeyword())
                        .language(searchRequest.getLocale())
                        .mustMatch(SearchMatchType.ANY))
                .build();
    }

    private SearchQuery createStoreQuery(String storeId) {
        return SearchExactExpressionBuilder.of()
                .exact(searchExactValueBuilder -> searchExactValueBuilder
                        .field("stores")
                        .value(storeId)
                        .fieldType(SearchFieldType.SET_REFERENCE))
                .build();
    }

    private ProductSearchProjectionParams createProductProjectionParams(SearchRequest searchRequest) {
        return ProductSearchProjectionParamsBuilder.of()
                .storeProjection(searchRequest.getStoreKey())
                .priceCurrency(searchRequest.getCurrency())
                .priceCountry(searchRequest.getCountry())
                .build();
    }

}
