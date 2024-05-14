package com.ecart.models;

public class Product {
    private int productId;
    private int productCategoryId;
    private String productName;
    private double price;
    private String imageUrl;
    private int productStock;
    private double productGST;
    private double discountAmount;
    private boolean deliverable;
    private double shippingCharge; // Added shipping charge attribute
    private double shippingDiscountAmount; // Added shipping discount attribute
    private int quantity;

    public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// Default constructor
    public Product() {
    }

    // Parameterized constructor
    public Product(int productId, int productCategoryId, String productName, double price, String imageUrl, int productStock, double productGST, double discountAmount, boolean deliverable, double shippingCharge, double shippingDiscountAmount) {
        this.productId = productId;
        this.productCategoryId = productCategoryId;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productStock = productStock;
        this.productGST = productGST;
        this.discountAmount = discountAmount;
        this.deliverable = deliverable;
        this.shippingCharge = shippingCharge;
        this.shippingDiscountAmount = shippingDiscountAmount;
    }

    // Getters and setters for each field

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public double getProductGST() {
        return productGST;
    }

    public void setProductGST(double productGST) {
        this.productGST = productGST;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public boolean isDeliverable() {
        return deliverable;
    }

    public void setDeliverable(boolean deliverable) {
        this.deliverable = deliverable;
    }

    public double getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(double shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public double getShippingDiscountAmount() {
        return shippingDiscountAmount;
    }

    public void setShippingDiscountAmount(double shippingDiscountAmount) {
        this.shippingDiscountAmount = shippingDiscountAmount;
    }
}
