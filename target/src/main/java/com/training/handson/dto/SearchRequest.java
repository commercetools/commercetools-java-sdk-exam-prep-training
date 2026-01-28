package com.training.handson.dto;

public class SearchRequest {
    private String locale;
    private String keyword;
    private String storeKey;
    private boolean facets;
    private String country;
    private String currency;

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setStoreKey(String storeKey) {
        this.storeKey = storeKey;
    }

    public void setFacets(boolean facets) {
        this.facets = facets;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocale() {
        return locale;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getStoreKey() {
        return storeKey;
    }

    public boolean isFacets() {
        return facets;
    }

    public String getCountry() {
        return country;
    }

    public String getCurrency() {
        return currency;
    }
}