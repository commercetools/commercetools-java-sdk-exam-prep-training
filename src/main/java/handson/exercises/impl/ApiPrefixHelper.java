package handson.exercises.impl;

public enum ApiPrefixHelper {
    API_POC_CLIENT_PREFIX("mh-exam-prep-poc-admin."),           // Your POC admin+import client
    API_CONC_CLIENT_PREFIX("happy-garden-conc-product-read."),  // Happy Garden Product Manage
    API_DEV_CLIENT_PREFIX("not set"),
    API_TEST_CLIENT_PREFIX("not set"),
    API_DEV_IMPORT_PREFIX("not set"),
    API_STORE_CLIENT_PREFIX("not set"),
    API_STORE_ME_CLIENT_PREFIX("not set"),
    API_ME_CLIENT_PREFIX("not set");

    private final String prefix;

    ApiPrefixHelper(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }
}