package com.ecart.models;


import java.util.List;

public class Address {
    private int addressId;
    private String userName;
    private String customerName;
    private String mobile;
    private String email;
    private int pincode;
    private String address;
    private boolean deliverable; // New attribute for deliverable status

    // Default constructor
    public Address() {
    }

    // Parameterized constructor
    public Address(int addressId, String userName, String customerName, String mobile, String email, int pincode, String address, boolean deliverable) {
        this.addressId = addressId;
        this.userName = userName;
        this.customerName = customerName;
        this.mobile = mobile;
        this.email = email;
        this.pincode = pincode;
        this.address = address;
        this.deliverable = deliverable;
    }

    // Getters and setters
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDeliverable() {
        return deliverable;
    }

    public void setDeliverable(boolean deliverable) {
        this.deliverable = deliverable;
    }
}
