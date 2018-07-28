package com.example.chinmay.anew.model;

/*{"retailerData":{"retailerId":33,"enterpriseName":"test shop name","mobileNo":"8888888888",
"addLine1":"test address line 1","subLocality1":"BIT Campus","locality":"Patna, Bihar, India","shopPhoto":"/public/33/sp.jpeg",
"latloc":25.596382,"longloc":85.088379,"deliveryStatus":1,"openCloseIsManual":1,"shopOpenTime1":"10:00:00","shopCloseTime1":"15:00:00",
"shopOpenTime2":"17:00:00","shopCloseTime2":"21:00:00","currentState":0,"lastStatusUpdate":"2018-07-27T12:10:24.000Z","verifiedByTeam":1},
"retailerProduct":[{"retailerId":33,"productId":1,"price":386},
{"retailerId":33,"productId":2,"price":390}]}*/

import java.util.ArrayList;

public class RetailerDetail {

    private String retailerId;
    private String enterpriseName;
    private String mobileNo;
    private String addLine1;
    private String subLocality1;
    private String locality;
    private String shopPhoto;
    private Double latloc;
    private Double longloc;
    private String deliveryStatus;
    private String openCloseIsManual;
    private String shopOpenTime1;
    private String shopCloseTime1;
    private String shopOpenTime2;
    private String shopCloseTime2;
    private String currentState;
    private String lastStatusUpdate;
    private String verifiedByTeam;
    private ArrayList<RetailerProduct> retailerProducts;
    private String cvs;

    public RetailerDetail() {
    }

    public RetailerDetail(String retailerId, String enterpriseName, String mobileNo, String addLine1, String subLocality1, String locality, String shopPhoto, Double latloc, Double longloc, String deliveryStatus, String openCloseIsManual, String shopOpenTime1, String shopCloseTime1, String shopOpenTime2, String shopCloseTime2, String currentState, String lastStatusUpdate, String verifiedByTeam, ArrayList<RetailerProduct> retailerProducts, String cvs) {
        this.retailerId = retailerId;
        this.enterpriseName = enterpriseName;
        this.mobileNo = mobileNo;
        this.addLine1 = addLine1;
        this.subLocality1 = subLocality1;
        this.locality = locality;
        this.shopPhoto = shopPhoto;
        this.latloc = latloc;
        this.longloc = longloc;
        this.deliveryStatus = deliveryStatus;
        this.openCloseIsManual = openCloseIsManual;
        this.shopOpenTime1 = shopOpenTime1;
        this.shopCloseTime1 = shopCloseTime1;
        this.shopOpenTime2 = shopOpenTime2;
        this.shopCloseTime2 = shopCloseTime2;
        this.currentState = currentState;
        this.lastStatusUpdate = lastStatusUpdate;
        this.verifiedByTeam = verifiedByTeam;
        this.retailerProducts = retailerProducts;
        this.cvs = cvs;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getAddLine1() {
        return addLine1;
    }

    public String getSubLocality1() {
        return subLocality1;
    }

    public String getLocality() {
        return locality;
    }

    public String getShopPhoto() {
        return shopPhoto;
    }

    public Double getLatloc() {
        return latloc;
    }

    public Double getLongloc() {
        return longloc;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getOpenCloseIsManual() {
        return openCloseIsManual;
    }

    public String getShopOpenTime1() {
        return shopOpenTime1;
    }

    public String getShopCloseTime1() {
        return shopCloseTime1;
    }

    public String getShopOpenTime2() {
        return shopOpenTime2;
    }

    public String getShopCloseTime2() {
        return shopCloseTime2;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getLastStatusUpdate() {
        return lastStatusUpdate;
    }

    public String getVerifiedByTeam() {
        return verifiedByTeam;
    }

    public ArrayList<RetailerProduct> getRetailerProducts() {
        return retailerProducts;
    }

    public String getCvs() {
        return cvs;
    }
}