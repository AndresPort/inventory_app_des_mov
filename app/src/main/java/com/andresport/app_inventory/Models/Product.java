package com.andresport.app_inventory.Models;

public class Product {
    //ATRIBUTES
    private String productRef;
    private String productName;
    private double unitPrice;
    private int stock;

    //Methods
    //Empty Constructor
    public Product() {
    }

    //Full Constructor
    public Product(String productRef, String productName, Double unitPrice, Integer stock) {
        this.productRef = productRef;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.stock = stock;
    }

    //Getters And Setters

    public String getProductRef() {
        return productRef;
    }

    public void setProductRef(String productRef) {
        this.productRef = productRef;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
