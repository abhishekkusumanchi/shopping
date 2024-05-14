package com.ecart.models;


public class ProductCategory {
    private int categoryId;
    private String title;

    // Default constructor
    public ProductCategory() {
    }

    // Parameterized constructor
    public ProductCategory(int categoryId, String title) {
        this.categoryId = categoryId;
        this.title = title;
    }

    // Getters and setters for each field

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

 
}

