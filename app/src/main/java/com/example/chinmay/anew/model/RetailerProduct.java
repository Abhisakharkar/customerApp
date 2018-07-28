package com.example.chinmay.anew.model;

// {"retailerId":33,"productId":1,"price":386}

public class RetailerProduct {

    private String productId;
    private int price;

    public RetailerProduct(String productId, int price) {
        this.productId = productId;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }
}
