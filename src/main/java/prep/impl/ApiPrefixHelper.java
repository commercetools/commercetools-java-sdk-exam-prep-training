package prep.impl;

public enum ApiPrefixHelper {
    API_POC_CLIENT_PREFIX("exam-prep-project-admin."),           // Your Trial admin+import client
//    API_POC_CLIENT_PREFIX("mh-exam-prep-poc-admin."),           // Your POC admin+import client
    API_SRC_CLIENT_PREFIX("import-src-project-read.");  // Source project API client

    private final String prefix;

    ApiPrefixHelper(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }
}