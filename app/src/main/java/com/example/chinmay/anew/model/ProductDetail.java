package com.example.chinmay.anew.model;

/*{"responseFrom":"magento_get_product_with_ids","items":[{"id":1,"sku":"Black Forest 0.5kg egg cake","name":"Black Forest 0.5kg egg cake",
"attribute_set_id":9,"price":350,"custom_attributes":[{"attribute_code":"meta_title","value":"Black Forest 0.5kg egg cake"},
{"attribute_code":"meta_keyword","value":"Black Forest 0.5kg egg cake"},{"attribute_code":"meta_description","value":"Black Forest 0.5kg egg cake "},
{"attribute_code":"image","value":"/b/l/blackforest.jpeg"},{"attribute_code":"small_image","value":"/b/l/blackforest.jpeg"},
{"attribute_code":"thumbnail","value":"/b/l/blackforest.jpeg"},{"attribute_code":"category_ids","value":["4"]},
{"attribute_code":"options_container","value":"container2"},{"attribute_code":"required_options","value":"0"},{"attribute_code":"has_options","value":"0"},
{"attribute_code":"url_key","value":"black-forest-0-5kg-egg-cake"},{"attribute_code":"gift_message_available","value":"0"},
{"attribute_code":"swatch_image","value":"/b/l/blackforest.jpeg"},{"attribute_code":"tax_class_id","value":"2"}]},*/

public class ProductDetail {

    private String id;
    private String sku;
    private String name;
    private String attributeSetId;
    private String price;
    private String image;

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
}
