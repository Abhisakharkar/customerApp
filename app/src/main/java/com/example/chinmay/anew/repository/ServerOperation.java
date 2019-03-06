package com.example.chinmay.anew.repository;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.Resource;
import com.example.chinmay.anew.GpsTracker;
import com.example.chinmay.anew.fragment.Categories;
import com.example.chinmay.anew.model.Category;
import com.example.chinmay.anew.model.ProductDetail;
import com.example.chinmay.anew.model.RetailerDetail;
import com.example.chinmay.anew.model.RetailersList;
import com.example.chinmay.anew.utils.JsonUtils;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ServerOperation {

    private String CategoriesUrl = "http://ec2-13-234-45-216.ap-south-1.compute.amazonaws.com:6868/magento_get_categories";
    private String RetailerDetailUrl = "http://ec2-13-234-45-216.ap-south-1.compute.amazonaws.com:6868/get_details_and_product_of_retailer";
    private String ProductDetailUrl = "http://ec2-13-234-45-216.ap-south-1.compute.amazonaws.com:6868/magento_get_product_with_ids";

    private String length,localityTier,localityId;
    private RequestQueue requestQueue;
    private int flag=0;
    private static String place;
    private GpsTracker gpsTracker;
    private double latitude;
    private double longitude;
    private int count=0;
    public static ArrayList<RetailersList> retailersArray;
    private HashMap params;
    private JSONObject locationParameters,retailerParameters;
    private Context context;
    private String locationURL="http://ec2-13-234-45-216.ap-south-1.compute.amazonaws.com:6868/get_location_ids";
    private String RetailersURL="http://ec2-13-234-45-216.ap-south-1.compute.amazonaws.com:6868/get_retailers_near_me";

    private String photoUrl="http://ec2-13-234-45-216.ap-south-1.compute.amazonaws.com/magento/pub/media/catalog/product";

    private String subLocality1Id;

    public ServerOperation(Context ctx){
        retailersArray=new ArrayList<>();
        context=ctx;
    }

    public ArrayList<RetailersList> getRetailersArray() {
        return retailersArray;
    }

    public String getLocation() {
        return place;
    }

    void requestServerRetailers() {
        getRetailersParameters();
        if(retailerParameters!=null) {
            JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST, RetailersURL, retailerParameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {


                                retailersArray.clear();
                                JSONArray retailersObject = response.getJSONArray("retailers");
                                for (int i = 0; i < retailersObject.length(); i++) {
                                    JSONObject jsonobject = retailersObject.getJSONObject(i);
                                    String retailersId=jsonobject.getString("retailerId");
                                    String enterpriseName=jsonobject.getString("enterpriseName");
                                    String mobileNo=jsonobject.getString("mobileNo");
                                    String shopPhoto=jsonobject.getString("shopPhoto");
                                    String subLocality1Id=jsonobject.getString("subLocality1Id");
                                    String localityId=jsonobject.getString("localityId");
                                    String latloc=jsonobject.getString("latloc");
                                    String longloc=jsonobject.getString("longloc");
                                    String deliveryStatus=jsonobject.getString("deliveryStatus");
                                    RetailersList r1=new RetailersList(retailersId,enterpriseName,mobileNo,shopPhoto,subLocality1Id,localityId,latloc,longloc,deliveryStatus);
                                    retailersArray.add(r1);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            Log.e("Volley", "Error");
                        }
                    }
            );
            requestQueue.add(obreq);
        }



    }

    private void getRetailersParameters() {

        if (localityTier.equals("0")) {
            params = new HashMap();
            params.put("localityId", localityId);
            retailerParameters = new JSONObject(params);
        }
        else {
            params = new HashMap();
            params.put("localityId", localityId);
            params.put("subLocality1Id", subLocality1Id);
            retailerParameters = new JSONObject(params);
        }
    }


    public void requestServerLocation() {
        requestQueue = Volley.newRequestQueue(context);
        getLocationParameters();
        if(locationParameters!=null) {
            JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST,locationURL , locationParameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                length = response.getString("length");
                                JSONObject localityData = response.getJSONObject("localityData");
                                localityId=localityData.getString("localityId");
                                localityTier = localityData.getString("tier");
                                requestServerRetailers();
                                if (localityTier.equals("0")) {
                                    place = localityData.getString("locality");
                                    flag = 1;
                                } else {
                                    JSONObject subLocality1Data = localityData.getJSONObject("subLocality1Data");
                                    subLocality1Id=response.getString("localityId");
                                    String tier1 = subLocality1Data.getString("tier");
                                    if (tier1.equals("0")) {
                                        place = subLocality1Data.getString("subLocality1");
                                        flag = 1;
                                    } else {
                                        JSONObject subLocality2Data = localityData.getJSONObject("subLocality2Data");
                                        String tier2 = subLocality2Data.getString("tier");
                                        if (tier2.equals("0")) {
                                            place = subLocality2Data.getString("subLocality2");
                                            flag = 1;
                                        }
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        // Handles errors that occur due to Volley
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            Log.e("Volley", "Error");
                        }
                    }
            );
            requestQueue.add(obreq);
        }
        else {
            getLocationParameters();
        }
    }
    private void getLocationParameters() {
        gpsTracker = new GpsTracker(context);
        if(gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            params = new HashMap();
            params.put("latloc", ""+latitude);
            params.put("longloc", ""+longitude);
            locationParameters = new JSONObject(params);
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public void requestServerLocationWithParameters(Double lat, Double longi) {
        requestQueue = Volley.newRequestQueue(context);
        params = new HashMap();
        params.put("latloc", ""+lat);
        params.put("longloc", ""+longi);
        locationParameters = new JSONObject(params);

        if(locationParameters!=null) {

            JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST, locationURL, locationParameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                length = response.getString("length");


                                JSONObject localityData = response.getJSONObject("localityData");
                                localityId=localityData.getString("localityId");
                                localityTier = localityData.getString("tier");
                                requestServerRetailers();
                                if (localityTier.equals("0")) {
                                    place = localityData.getString("locality");
                                    flag = 1;
                                } else {
                                    JSONObject subLocality1Data = localityData.getJSONObject("subLocality1Data");
                                    subLocality1Id=response.getString("localityId");
                                    String tier1 = subLocality1Data.getString("tier");
                                    if (tier1.equals("0")) {
                                        place = subLocality1Data.getString("subLocality1");
                                        flag = 1;
                                    } else {
                                        JSONObject subLocality2Data = localityData.getJSONObject("subLocality2Data");
                                        String tier2 = subLocality2Data.getString("tier");
                                        if (tier2.equals("0")) {
                                            place = subLocality2Data.getString("subLocality2");
                                            flag = 1;
                                        }
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        // Handles errors that occur due to Volley
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            Log.e("Volley", "Error");
                        }
                    }
//
//            }
            );
            // Adds the JSON object request "obreq" to the request queue
            requestQueue.add(obreq);
//
        }
        else
        {
            getLocationParameters();
        }
    }

    public MutableLiveData<ArrayList<Category>> getCategories(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonBody = new JSONObject();
        final MutableLiveData<ArrayList<Category>> categoryArrayList = new MutableLiveData<>();
        try {
            jsonBody = new JSONObject("{\"type\":\"example\"}");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,CategoriesUrl,jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JsonUtils jsonUtils = new JsonUtils();
                    ArrayList<Category> category = jsonUtils.parseCategoryJson(String.valueOf(response));
                    categoryArrayList.setValue(category);
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Log.e("Volley", "Error");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        return categoryArrayList;
    }

    public MutableLiveData<RetailerDetail> getRetailerDetail(String id){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        HashMap parameters = new HashMap();
        parameters.put("retailerId",id);
        JSONObject jsonObject = new JSONObject(parameters);

        final MutableLiveData<RetailerDetail> mutableLiveData = new MutableLiveData<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,RetailerDetailUrl,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JsonUtils jsonUtils = new JsonUtils();
                    RetailerDetail retailerDetail = jsonUtils.parseRetailerDetail(String.valueOf(response));
                    mutableLiveData.setValue(retailerDetail);
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Log.e("Volley", "Error");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        return mutableLiveData;

    }

    public MutableLiveData<ArrayList<ProductDetail>> getProductDetails(String id){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        HashMap parameters = new HashMap();
        parameters.put("Ids",id);
        JSONObject jsonObject = new JSONObject(parameters);

        final MutableLiveData<ArrayList<ProductDetail>> mutableLiveData = new MutableLiveData<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,ProductDetailUrl,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JsonUtils jsonUtils = new JsonUtils();
                    ArrayList<ProductDetail> productDetails = jsonUtils.parseProductDetail(String.valueOf(response));
                    mutableLiveData.setValue(productDetails);
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Log.e("Volley", "Error");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        return mutableLiveData;
    }

}