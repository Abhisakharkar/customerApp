package com.example.chinmay.anew.utils;

import android.util.Log;

import com.example.chinmay.anew.model.Category;
import com.example.chinmay.anew.model.ProductDetail;
import com.example.chinmay.anew.model.RetailerDetail;
import com.example.chinmay.anew.model.RetailerProduct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static ArrayList<Category> parseCategoryJson(String json) {
        if (json != null) {
            ArrayList<Category> category = new ArrayList<>();
            try {
                JSONObject jsonObj = new JSONObject(json);
                JSONArray itemsArray = jsonObj.getJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonobject = itemsArray.getJSONObject(i);
                    String name = jsonobject.getString("name");
                    String url = "";
                    String id = jsonobject.getString("id");
                    String level = jsonobject.getString("level");
                    String child = jsonobject.getString("children");
                    ArrayList<Integer> children = new ArrayList<>();
                    for(int j=0;j<child.length();j++){
                        try {
                            int num = Integer.parseInt(child.substring(j,j+1));
                            children.add(num);
                        }catch (Exception e){

                        }
                    }
                    category.add(new Category(name,url,level,id,children));
                }
            } catch (Exception e) {
                return null;
            }

            return category;
        }else{
            return null;
        }
    }

    public static RetailerDetail parseRetailerDetail(String json){
        if(json != null){
            RetailerDetail retailerDetails;
            try {
                JSONObject itemsArray = new JSONObject(json);
                JSONObject  jsonObj = itemsArray.getJSONObject("retailerData");

                String retailerId = jsonObj.getString("retailerId");
                String enterpriseName = jsonObj.getString("enterpriseName");
                String mobileNo = jsonObj.getString("mobileNo");
                String addLine1 = jsonObj.getString("addLine1");
                String subLocality1 = jsonObj.getString("subLocality1");
                String locality = jsonObj.getString("locality");
                String shopPhoto = jsonObj.getString("shopPhoto");
                Double latloc = jsonObj.getDouble("latloc");
                Double longloc = jsonObj.getDouble("longloc");
                String deliveryStatus = jsonObj.getString("deliveryStatus");
                String openCloseIsManual = jsonObj.getString("openCloseIsManual");
                String shopOpenTime1 = jsonObj.getString("shopOpenTime1");
                String shopCloseTime1 = jsonObj.getString("shopCloseTime1");
                String shopOpenTime2 = jsonObj.getString("shopOpenTime2");
                String shopCloseTime2 = jsonObj.getString("shopCloseTime2");
                String currentState = jsonObj.getString("currentState");
                String lastStatusUpdate = jsonObj.getString("lastStatusUpdate");
                String verifiedByTeam = jsonObj.getString("verifiedByTeam");

                JSONArray retailerProduct = itemsArray.getJSONArray("retailerProduct");
                ArrayList<RetailerProduct> retailerProducts = new ArrayList<>();
                String cvs = "";
                for(int i=0;i<retailerProduct.length();i++){
                    JSONObject jsonobject = retailerProduct.getJSONObject(i);
                    String productId = jsonobject.getString("productId");
                    int price = jsonobject.getInt("price");
                    retailerProducts.add(new RetailerProduct(productId,price));
                    cvs += productId;
                    if(i != retailerProduct.length()-1){
                        cvs += ",";
                    }
                }

                retailerDetails = (new RetailerDetail(retailerId,enterpriseName,mobileNo,addLine1, subLocality1,locality,shopPhoto,latloc,longloc,deliveryStatus,openCloseIsManual,shopOpenTime1,shopCloseTime1, shopOpenTime2,shopCloseTime2,currentState,lastStatusUpdate,verifiedByTeam,retailerProducts,cvs));

            } catch (Exception e) {
                Log.e("Message",e.getMessage());
                return null;
            }
            return retailerDetails;

        }else{
            return null;
        }
    }

    public static ArrayList<ProductDetail> parseProductDetail(String json){
        if(json != null){
            ArrayList<ProductDetail> productDetails = new ArrayList<>();
            try{
                JSONObject jsonObj = new JSONObject(json);
                JSONArray itemsArray = jsonObj.optJSONArray("items");
                for (int i=0;i<itemsArray.length();i++){
                    JSONObject jsonobject = itemsArray.getJSONObject(i);

                    String id = jsonobject.getString("id");
                    String sku = jsonobject.getString("sku");
                    String name = jsonobject.getString("name");
                    String attributeSetId = jsonobject.getString("attribute_set_id");
                    String price = jsonobject.getString("price");
                    String image = null;

                    JSONObject tempObj = itemsArray.optJSONObject(i);
                    JSONArray imageArray = tempObj.getJSONArray("custom_attributes");
                    for(int j=0; j<imageArray.length();j++){
                        JSONObject jsonImgObj = imageArray.getJSONObject(j);
                        String attributeCode = jsonImgObj.getString("attribute_code");
                        if(attributeCode.equalsIgnoreCase("image")){
                            image = jsonImgObj.getString("value");
                        }
                    }

                    productDetails.add(new ProductDetail(id,sku,name,attributeSetId,price,image));

                }

            }catch (Exception e){
                return null;
            }
            return productDetails;
        }
        return null;
    }

}
