package com.example.chinmay.anew;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaceAPI {

    private static final String TAG = PlaceAPI.class.getSimpleName();

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private ArrayList<MyGooglePlaces> temp;
    private Context context;

    private static final String API_KEY = "AIzaSyB2t4PAMR5gqD--qzc3_33P1jG1rs6mC-g";
    private GpsTracker gpsTracker;
    private double currentLatitude,currentLongitude;

    public ArrayList<MyGooglePlaces> autocomplete (String input, Context ctx) {
        ArrayList<MyGooglePlaces> resultList = null;
        context=ctx;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
             temp = new ArrayList<>();
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=(cities)");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            LatLng latlong=getPosition();

            String url1= "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+input+"&location="+latlong.latitude+","+latlong.longitude+"&radius=10000&key="+API_KEY;


            URL url = new URL(url1);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return temp;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return temp;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            temp = new ArrayList<>();

            // Log.d(TAG, jsonResults.toString());

            // Create a JSON object hierarchy from the results
            JSONObject jsonObject = new JSONObject(jsonResults.toString());
            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    MyGooglePlaces poi = new MyGooglePlaces();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));
                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("YES");
                                } else {
                                    poi.setOpenNow("NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("formatted_address"))
                        {
                            poi.setAddress(jsonArray.getJSONObject(i).optString("formatted_address"));
                        }
                        if (jsonArray.getJSONObject(i).has("geometry"))
                        {
                            if (jsonArray.getJSONObject(i).getJSONObject("geometry").has("location"))
                            {
                                if (jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").has("lat"))
                                {
                                    poi.setLatitude(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat")));
                                    poi.setLongitude(Double.parseDouble(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng")));
                                }
                            }
                        }
                        if (jsonArray.getJSONObject(i).has("vicinity")) {
                            poi.setVicinity(jsonArray.getJSONObject(i).optString("vicinity"));
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");
                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
                    }
                    //if(temp.size()<5)
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return temp;
    }

    public LatLng getPosition() {
        LatLng lng;
        lng=new LatLng(79,21); // Default

        gpsTracker = new GpsTracker(context);
        if(gpsTracker.canGetLocation())
        {
            currentLatitude = gpsTracker.getLatitude();
            currentLongitude = gpsTracker.getLongitude();

             lng=new LatLng(currentLatitude,currentLongitude);
        }else{
            gpsTracker.showSettingsAlert();
        }
        return lng;
    }
}