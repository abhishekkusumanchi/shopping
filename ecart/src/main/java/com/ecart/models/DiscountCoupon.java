package com.ecart.models;


import java.util.Date;

public class DiscountCoupon {
    private int dcpnId;
    private String dcpnCode;
    private double dcpnValue;
    private int noOfCpn;
    private double dcpnMinVal;
    private Date expireDate;

    // Default constructor
    public DiscountCoupon() {
    }

    // Parameterized constructor
    public DiscountCoupon(int dcpnId, String dcpnCode, double dcpnValue, int noOfCpn, double dcpnMinVal, Date expireDate) {
        this.dcpnId = dcpnId;
        this.dcpnCode = dcpnCode;
        this.dcpnValue = dcpnValue;
        this.noOfCpn = noOfCpn;
        this.dcpnMinVal = dcpnMinVal;
        this.expireDate = expireDate;
    }

    // Getters and setters for each field

    public int getDcpnId() {
        return dcpnId;
    }

    public void setDcpnId(int dcpnId) {
        this.dcpnId = dcpnId;
    }

    public String getDcpnCode() {
        return dcpnCode;
    }

    public void setDcpnCode(String dcpnCode) {
        this.dcpnCode = dcpnCode;
    }

    public double getDcpnValue() {
        return dcpnValue;
    }

    public void setDcpnValue(double dcpnValue) {
        this.dcpnValue = dcpnValue;
    }

    public int getNoOfCpn() {
        return noOfCpn;
    }

    public void setNoOfCpn(int noOfCpn) {
        this.noOfCpn = noOfCpn;
    }

    public double getDcpnMinVal() {
        return dcpnMinVal;
    }

    public void setDcpnMinVal(double dcpnMinVal) {
        this.dcpnMinVal = dcpnMinVal;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
