package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.product.ProductVariant;
import com.commercetools.importapi.models.common.LocalizedStringBuilder;
import com.commercetools.importapi.models.common.MoneyBuilder;
import com.commercetools.importapi.models.common.ProductTypeKeyReferenceBuilder;
import com.commercetools.importapi.models.common.TaxCategoryKeyReferenceBuilder;
import com.commercetools.importapi.models.productdrafts.*;
import com.commercetools.importapi.models.productvariants.Attribute;
import handson.exercises.impl.ClientService;
import handson.exercises.impl.ImportService;
import handson.exercises.impl.ProductService;
import handson.exercises.impl.ProductTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class Task1d_IMPORT_PRODUCTS {

    // TODO
    // Fix Error
    // Import attributes
    // Get the product type key

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Import API: Product Import

        // TODO Step 1: Provide your container key, product type key, and tax category key
        final String containerKey = "mh-product-data-container"; // to be created
        final String productTypeKey = "flowers-product-type";
        final String taxCategoryKey = "standard-tax";

        Logger logger = LoggerFactory.getLogger("commercetools");

        final com.commercetools.importapi.client.ProjectApiRoot apiRoot =
                ClientService.createImportApiClient("import");
        ImportService importService = new ImportService(apiRoot);

        final ProjectApiRoot apiRoot_src = ClientService.createApiClient("happy-garden-src-project-read");
        ProductService productService_source = new ProductService(apiRoot_src);
        ProductTypeService productTypeService_Source = new ProductTypeService(apiRoot_src);

        // Get the products from the source project
        final List<Product> products = productTypeService_Source.getProductTypeByKey(productTypeKey)
                .thenComposeAsync(productTypeApiHttpResponse ->
                        productService_source.getProductsByProductTypeId(
                                productTypeApiHttpResponse.getBody().getId()))
                .get()
                .getBody().getResults();

        // TODO Step 1: Import one product to the project
        //
        final ProductDraftImport productDraftImport = ProductDraftImportBuilder.of()
                .key(products.get(0).getKey())
                .description(
                        LocalizedStringBuilder.of()
                                .values(products.get(0).getMasterData().getCurrent().getDescription().values())
                                .build()
                )
                .masterVariant(createProductVariantDraftImport(products.get(0).getMasterData().getCurrent().getMasterVariant()))
                .variants(products.get(0).getMasterData().getCurrent().getVariants()
                        .stream().map(
                                productVariant -> createProductVariantDraftImport(productVariant)
                        ).collect(Collectors.toList())
                )
                .name(LocalizedStringBuilder.of()
                        .values(products.get(0).getMasterData().getCurrent().getName().values())
                        .build()
                )
                .productType(
                        ProductTypeKeyReferenceBuilder.of()
                                .key(productTypeKey)                // where to get from?
                                .build()
                )
                .slug(LocalizedStringBuilder.of()
                        .values(products.get(0).getMasterData().getCurrent().getSlug().values())
                        .build()
                )
                .taxCategory(TaxCategoryKeyReferenceBuilder.of()
                        .key(taxCategoryKey)
                        .build()
                )
                .publish(true)
                .build();

        // Import product draft
        logger.info("Product import operation Id: " +
                    ""
                );

        apiRoot.close();
        apiRoot_src.close();
    }

    private static ProductVariantDraftImport createProductVariantDraftImport(
            final ProductVariant productVariant){
        return ProductVariantDraftImportBuilder.of()
                .key(productVariant.getKey())
                .sku(productVariant.getSku())
                .attributes(
                        Arrays.asList(
                                Attribute.textBuilder()
                                        .name(productVariant.getAttributes().get(0).getName())
                                        .value(productVariant.getAttributes().get(0).getValue().toString())
                                        .build(),
                                Attribute.numberBuilder()
                                        .name(productVariant.getAttributes().get(1).getName())
                                        .value(convertDouble(productVariant.getAttributes().get(1).getValue()))
                                        .build()
                        )
                )
                .prices(productVariant.getPrices()
                        .stream()
                        .map(price -> PriceDraftImportBuilder.of()
                                .key(price.getKey())
                                .country(price.getCountry())
                                .value(MoneyBuilder.of()
                                        .centAmount(price.getValue().getCentAmount())
                                        .currencyCode(price.getValue().getCurrencyCode())
                                        .build()
                                )
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }
    static double convertDouble(Object attributeValue){
        if(attributeValue instanceof Long)
            return ((Long) attributeValue).doubleValue();
        else
            return ((Double) attributeValue);
    }
}
