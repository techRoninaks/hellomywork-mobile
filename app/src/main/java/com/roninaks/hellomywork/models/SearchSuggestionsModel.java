package com.roninaks.hellomywork.models;

public class SearchSuggestionsModel {
    private String ServiceProvider;
    private String CategoryName;
    private int CategoryId;
    private int LocationId;

    //Getters
    public int getCategoryId() {
        return CategoryId;
    }

    public int getLocationId() {
        return LocationId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getServiceProvider() {
        return ServiceProvider;
    }

    //Setters
    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public void setLocationId(int locationId) {
        LocationId = locationId;
    }

    public void setServiceProvider(String serviceProvider) {
        ServiceProvider = serviceProvider;
    }
}
