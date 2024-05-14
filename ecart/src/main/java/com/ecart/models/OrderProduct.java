package com.ecart.models;


public class OrderProduct {
    private int orderId;
    private int productId;
    private int quantity;
    private double price;
    private double totalGst;

    // Default constructor
    public OrderProduct() {
    }

    // Parameterized constructor
    public OrderProduct(int orderId, int productId, int quantity, double price, double totalGst) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.totalGst = totalGst;
    }

    // Getters and setters for each field

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalGst() {
        return totalGst;
    }

    public void setTotalGst(double totalGst) {
        this.totalGst = totalGst;
    }
}
