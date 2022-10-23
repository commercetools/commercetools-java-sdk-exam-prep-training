package handson.exercises;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.Product;
import com.commercetools.importapi.models.common.LocalizedStringBuilder;
import com.commercetools.importapi.models.common.MoneyBuilder;
import com.commercetools.importapi.models.common.ProductTypeKeyReferenceBuilder;
import com.commercetools.importapi.models.productdrafts.PriceDraftImportBuilder;
import com.commercetools.importapi.models.productdrafts.ProductDraftImport;
import com.commercetools.importapi.models.productdrafts.ProductDraftImportBuilder;
import com.commercetools.importapi.models.productdrafts.ProductVariantDraftImportBuilder;
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

        // TODO Step 1: Provide your container key
        final String containerKey = "XX-exam-prep-product-data-container";

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
        // ERROR
        //
        final ProductDraftImport productDraftImport = ProductDraftImportBuilder.of()
                .key(products.get(0).getKey())
                .description(
                        LocalizedStringBuilder.of()
                                .values(products.get(0).getMasterData().getCurrent().getDescription().values())
                                .build()
                )
                .masterVariant(
                        ProductVariantDraftImportBuilder.of()
                            .key(products.get(0).getMasterData().getCurrent().getMasterVariant().getKey())
                            .sku(products.get(0).getMasterData().getCurrent().getMasterVariant().getSku())
                                .attributes(
                                        Arrays.asList(
                                                Attribute.textBuilder()
                                                        .name(products.get(0).getMasterData().getCurrent().getMasterVariant().getAttributes().get(0).getName())
                                                        .value(products.get(0).getMasterData().getCurrent().getMasterVariant().getAttributes().get(0).getValue().toString())
                                                        .build(),
                                                Attribute.numberBuilder()
                                                        .name(products.get(0).getMasterData().getCurrent().getMasterVariant().getAttributes().get(1).getName())
                                                        .value(Double.longBitsToDouble((Long)(products.get(0).getMasterData().getCurrent().getMasterVariant().getAttributes().get(1).getValue())))
                                                        .build()
                                        )
                                )
                            .prices(products.get(0).getMasterData().getCurrent().getMasterVariant().getPrices()
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
                            .build()
                        )

                .variants(ProductVariantDraftImportBuilder.of()
                        .key(products.get(0).getMasterData().getCurrent().getVariants().get(0).getKey())
                        .sku(products.get(0).getMasterData().getCurrent().getVariants().get(0).getSku())
                        .attributes(
                                Arrays.asList(
                                        Attribute.textBuilder()
                                                .name(products.get(0).getMasterData().getCurrent().getVariants().get(0).getAttributes().get(0).getName())
                                                .value(products.get(0).getMasterData().getCurrent().getVariants().get(0).getAttributes().get(0).getValue().toString())
                                                .build(),
                                        Attribute.numberBuilder()
                                                .name(products.get(0).getMasterData().getCurrent().getVariants().get(0).getAttributes().get(1).getName())
                                                .value(Double.longBitsToDouble((Long)products.get(0).getMasterData().getCurrent().getVariants().get(0).getAttributes().get(1).getValue()))
                                                .build()
                                )
                        )
                        .prices(products.get(0).getMasterData().getCurrent().getVariants().get(0).getPrices()
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
                        .build()
                )


                .name(LocalizedStringBuilder.of()
                        .values(products.get(0).getMasterData().getCurrent().getName().values())
                        .build()
                )
                .productType(
                        ProductTypeKeyReferenceBuilder.of()
                                .key("flowers-product-type")                // where to get from?
                                .build()
                )
                .slug(LocalizedStringBuilder.of()
                        .values(products.get(0).getMasterData().getCurrent().getSlug().values())
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
}
