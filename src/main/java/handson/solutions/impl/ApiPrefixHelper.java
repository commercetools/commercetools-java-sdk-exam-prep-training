package handson.solutions.impl;

public enum ApiPrefixHelper {
    API_POC_CLIENT_PREFIX("nd-exam-prep-poc-admin."),           // Your POC admin+import client
    API_CONC_CLIENT_PREFIX("happy-garden-conc-project-read.");  // Happy Garden Product Manage

    private final String prefix;

    ApiPrefixHelper(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }
}