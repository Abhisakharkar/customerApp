package com.example.chinmay.anew.model;

/*{"responseFrom":"magento_get_product_with_ids","items":[{"id":1,"sku":"Black Forest 0.5kg egg cake","name":"Black Forest 0.5kg egg cake",
"attribute_set_id":9,"price":350,"custom_attributes":[{"attribute_code":"meta_title","value":"Black Forest 0.5kg egg cake"},
{"attribute_code":"meta_keyword","value":"Black Forest 0.5kg egg cake"},{"attribute_code":"meta_description","value":"Black Forest 0.5kg egg cake "},
{"attribute_code":"image","value":"/b/l/blackforest.jpeg"},{"attribute_code":"small_image","value":"/b/l/blackforest.jpeg"},
{"attribute_code":"thumbnail","value":"/b/l/blackforest.jpeg"},{"attribute_code":"category_ids","value":["4"]},
{"attribute_code":"options_container","value":"container2"},{"attribute_code":"required_options","value":"0"},{"attribute_code":"has_options","value":"0"},
{"attribute_code":"url_key","value":"black-forest-0-5kg-egg-cake"},{"attribute_code":"gift_message_available","value":"0"},
{"attribute_code":"swatch_image","value":"/b/l/blackforest.jpeg"},{"attribute_code":"tax_class_id","value":"2"}]},*/

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = "ProductTable")
public class ProductDetail {

    @PrimaryKey
    @NonNull
    private String id;
    private String price;
    private String image;
    private String name;
    private Date time;

    @Ignore
    private String sku;
    @Ignore
    private String attributeSetId;

    public ProductDetail(@NonNull String id, String price, String image, String name, Date time) {
        this.id = id;
        this.price = price;
        this.image = image;
        this.name = name;
        this.time = time;
    }

    @Ignore
    public ProductDetail(String id, String sku, String name, String attributeSetId, String price, String image) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.attributeSetId = attributeSetId;
        this.price = price;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getAttributeSetId() {
        return attributeSetId;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setAttributeSetId(String attributeSetId) {
        this.attributeSetId = attributeSetId;
    }

    public Date getTime() {
        return time;
    }
}
