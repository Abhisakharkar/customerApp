package com.example.chinmay.anew.model;

public class RetailersList {
    private String retailerId;
    private String enterpriseName;
    private String  mobileNo;
    private String  shopPhoto;
    private String  subLocality1Id;
    private String  localityId;
    private String latloc;
    private String longloc;
    private String deliveryStatus;

    public RetailersList(String retailerId, String enterpriseName, String mobileNo, String shopPhoto, String subLocality1Id, String localityId, String latloc, String longloc, String deliveryStatus) {
        this.setRetailerId(retailerId);
        this.setEnterpriseName(enterpriseName);
        this.setMobileNo(mobileNo);
        this.setShopPhoto(shopPhoto);
        this.setSubLocality1Id(subLocality1Id);
        this.setLocalityId(localityId);
        this.setLatloc(latloc);
        this.setLongloc(longloc);
        this.setDeliveryStatus(deliveryStatus);
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }



    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public String getSubLocality1Id() {
        return subLocality1Id;
    }

    public void setSubLocality1Id(String subLocality1Id) {
        this.subLocality1Id = subLocality1Id;
    }

    public String getLocalityId() {
        return localityId;
    }

    public void setLocalityId(String localityId) {
        this.localityId = localityId;
    }

    public String getLatloc() {
        return latloc;
    }

    public void setLatloc(String latloc) {
        this.latloc = latloc;
    }

    public String getLongloc() {
        return longloc;
    }

    public void setLongloc(String longloc) {
        this.longloc = longloc;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
