package com.ecart.models;

public class Coupon {
    private String code;
    private double value;
    private int numOfCoupons;
    private double minValue;
    private String expireDate;
    
    
    
	public Coupon() {
		
	}
	public Coupon(String code, double value, int numOfCoupons, double minValue, String expireDate) {
		this.code = code;
		this.value = value;
		this.numOfCoupons = numOfCoupons;
		this.minValue = minValue;
		this.expireDate = expireDate;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getNumOfCoupons() {
		return numOfCoupons;
	}
	public void setNumOfCoupons(int numOfCoupons) {
		this.numOfCoupons = numOfCoupons;
	}
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

    
}
