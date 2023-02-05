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
import handson.exercises.impl.ApiPrefixHelper;
import handson.exercises.impl.ImportService;
import handson.exercises.impl.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static handson.solutions.impl.ClientService.createApiClient;
import static handson.solutions.impl.ClientService.createImportApiClient;


public class Task1d_IMPORT_PRODUCTS {

    // TODO
    // Fix Error
    // Import attributes
    // Get the product type key

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // Learning Goals
        // Import API: Product Import

        // TODO Step 1: Provide your container key, product type key, and tax category key
        final String containerKey = "XX-exam-prep-product-data-container"; // to be created
        final String concProductTypeKey = "flowers-product-type";
        final String taxCategoryKey = "standard-tax-category";

        Logger logger = LoggerFactory.getLogger(Task1d_IMPORT_PRODUCTS.class.getName());

        final ProjectApiRoot apiRoot_conc =
                createApiClient(
                        ApiPrefixHelper.API_CONC_CLIENT_PREFIX.getPrefix()
                );
        final com.commercetools.importapi.client.ProjectApiRoot
            apiRoot_poc_import =
                createImportApiClient(
                        ApiPrefixHelper.API_POC_CLIENT_PREFIX.getPrefix()
                );
        ProductService productService = new ProductService(apiRoot_conc);
        ImportService importService = new ImportService(apiRoot_poc_import);

        // Get the products from conc
        final List<Product> products =
                productService.getProducts()
                .get()
                .getBody().getResults();

        // TODO Step 1: Import one product to poc
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
                                .key(concProductTypeKey)                // where to get from?
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

        apiRoot_conc.close();
        apiRoot_poc_import.close();
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
