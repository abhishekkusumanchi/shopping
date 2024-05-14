package com.ecart.models;

import java.time.LocalDate;
import java.util.List;

public class Order {
	private int orderId;
    private String userName;
    private String orderDate;
    private double orderTotal;
    private String discountCode;
    private int addressId;
    private List<OrderProduct> orderProducts;

    // Default constructor
    public Order() {
    }

    // Parameterized constructor
    public Order(int orderId, String userName, String orderDate, double orderTotal, String discountCode, int addressId, List<OrderProduct> orderProducts) {
        this.orderId = orderId;
        this.userName = userName;
        this.orderDate = orderDate;
        this.orderTotal = orderTotal;
        this.discountCode = discountCode;
        this.addressId = addressId;
        this.orderProducts = orderProducts;
    }

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}
    
}
